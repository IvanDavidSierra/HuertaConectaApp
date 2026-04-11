package co.ue.edu.huertaconectaapp.views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.model.Huerta;
import co.ue.edu.huertaconectaapp.model.Publicacion;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.PublicacionDao;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;
import co.ue.edu.huertaconectaapp.model.db.dao.UsuarioHuertaDao;
import co.ue.edu.huertaconectaapp.util.ImageUtils;
import co.ue.edu.huertaconectaapp.views.adapter.PublicacionAdapter;

public class HuertaPublicacionesFragment extends Fragment {

    private static final String ARG_HUERTA = "huerta";
    /** Si true y falta fila local usuarios_huertas, se inserta (p. ej. usuario vino de API en Mis huertas). */
    private static final String ARG_SINC_VINCULO = "sinc_vinculo";
    /** Solo ver publicaciones, sin crear (visitante / no miembro). */
    private static final String ARG_SOLO_LECTURA = "solo_lectura";

    private Huerta huerta;
    private boolean sincronizarVinculoSiFalta;
    private boolean soloLectura;
    private View toolbarPrincipal;
    private PublicacionDao publicacionDao;
    private SesionDao sesionDao;
    private UsuarioHuertaDao usuarioHuertaDao;

    private PublicacionAdapter adapter;
    private RecyclerView rv;
    private TextView tvVacio;
    private byte[] imagenPendiente;
    /** Clave foránea en publicaciones; evita fallar si id_usuario de sesión no coincide con la fila local. */
    private int idUsuariosHuertasCache = -1;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickImagen =
        registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri == null) return;
            try {
                imagenPendiente = ImageUtils.uriToJpegBlob(requireContext(), uri);
            } catch (IOException e) {
                imagenPendiente = null;
                Toast.makeText(getContext(), R.string.muro_error_imagen, Toast.LENGTH_SHORT).show();
            }
            actualizarPreviewDialog();
        });

    private ActivityResultLauncher<String> solicitudPermisoLectura;
    private Runnable despuesDePermiso;
    private AlertDialog dialogPublicar;
    private com.google.android.material.imageview.ShapeableImageView dialogImagePreview;

    public static HuertaPublicacionesFragment newInstance(Huerta huerta, boolean sincronizarVinculoSiFalta) {
        return newInstance(huerta, sincronizarVinculoSiFalta, false);
    }

    public static HuertaPublicacionesFragment newInstance(Huerta huerta, boolean sincronizarVinculoSiFalta,
                                                          boolean soloLectura) {
        HuertaPublicacionesFragment f = new HuertaPublicacionesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HUERTA, huerta);
        args.putBoolean(ARG_SINC_VINCULO, sincronizarVinculoSiFalta);
        args.putBoolean(ARG_SOLO_LECTURA, soloLectura);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            huerta = (Huerta) getArguments().getSerializable(ARG_HUERTA);
            sincronizarVinculoSiFalta = getArguments().getBoolean(ARG_SINC_VINCULO, false);
            soloLectura = getArguments().getBoolean(ARG_SOLO_LECTURA, false);
        }
        solicitudPermisoLectura = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            granted -> {
                if (granted && despuesDePermiso != null) {
                    despuesDePermiso.run();
                    despuesDePermiso = null;
                } else if (!granted) {
                    Toast.makeText(requireContext(), R.string.muro_permiso_denegado, Toast.LENGTH_LONG).show();
                }
            });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_huerta_publicaciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarPrincipal = requireActivity().findViewById(R.id.toolbar);
        if (toolbarPrincipal != null) {
            toolbarPrincipal.setVisibility(View.GONE);
        }

        MaterialToolbar tb = view.findViewById(R.id.toolbarMuro);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(tb);
        if (huerta != null) {
            tb.setTitle(huerta.getNombreHuerta());
            tb.setSubtitle(soloLectura ? R.string.muro_subtitulo_solo_lectura : R.string.muro_subtitulo);
        }
        tb.setNavigationOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
        publicacionDao = new PublicacionDao(db);
        sesionDao = new SesionDao(db);
        usuarioHuertaDao = new UsuarioHuertaDao(db);

        rv = view.findViewById(R.id.rvPublicaciones);
        tvVacio = view.findViewById(R.id.tvSinPublicaciones);
        if (soloLectura) {
            tvVacio.setText(R.string.muro_sin_publicaciones_solo_lectura);
        }
        ExtendedFloatingActionButton fab = view.findViewById(R.id.fabNuevaPublicacion);
        if (soloLectura) {
            fab.setVisibility(View.GONE);
            int pad = (int) (24 * getResources().getDisplayMetrics().density);
            rv.setPadding(rv.getPaddingLeft(), rv.getPaddingTop(), rv.getPaddingRight(), pad);
        }

        adapter = new PublicacionAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        if (huerta == null) {
            cerrarFragmentPorError(view);
            return;
        }
        if (!soloLectura && !asegurarAccesoMiembro()) {
            Toast.makeText(getContext(), R.string.muro_no_miembro, Toast.LENGTH_LONG).show();
            cerrarFragmentPorError(view);
            return;
        }

        if (!soloLectura) {
            fab.setOnClickListener(v -> mostrarDialogoNuevaPublicacion());
        }
        refrescarLista();
    }

    /** @return false si no hay membresía local y no se permite sincronizar */
    private boolean asegurarAccesoMiembro() {
        int idU = sesionDao.obtenerIdUsuario();
        int idH = huerta.getIdHuerta();

        idUsuariosHuertasCache = usuarioHuertaDao.obtenerIdUsuariosHuertas(idU, idH);
        if (idUsuariosHuertasCache >= 0) {
            return true;
        }
        if (sincronizarVinculoSiFalta) {
            String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            long rowId = usuarioHuertaDao.insertarConReferencias(idU, idH, fecha, huerta);
            if (rowId != -1) {
                idUsuariosHuertasCache = (int) rowId;
                return true;
            }
            idUsuariosHuertasCache = usuarioHuertaDao.obtenerIdUsuariosHuertas(idU, idH);
            if (idUsuariosHuertasCache >= 0) {
                return true;
            }
        }
        // Sesión con id_usuario distinto al guardado al unirse: reutilizar vínculo local existente
        idUsuariosHuertasCache = usuarioHuertaDao.obtenerPrimerIdUsuariosHuertasPorHuerta(idH);
        return idUsuariosHuertasCache >= 0;
    }

    private int resolverIdUsuariosHuertasParaPublicar() {
        if (idUsuariosHuertasCache >= 0) {
            return idUsuariosHuertasCache;
        }
        int idU = sesionDao.obtenerIdUsuario();
        int idH = huerta.getIdHuerta();
        int id = usuarioHuertaDao.obtenerIdUsuariosHuertas(idU, idH);
        if (id >= 0) {
            idUsuariosHuertasCache = id;
            return id;
        }
        id = usuarioHuertaDao.obtenerPrimerIdUsuariosHuertasPorHuerta(idH);
        if (id >= 0) {
            idUsuariosHuertasCache = id;
        }
        return id;
    }

    private void refrescarLista() {
        if (huerta == null) return;
        List<Publicacion> lista = publicacionDao.listarPorIdHuerta(huerta.getIdHuerta());
        adapter.setItems(lista);
        tvVacio.setVisibility(lista.isEmpty() ? View.VISIBLE : View.GONE);
        rv.setVisibility(lista.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void mostrarDialogoNuevaPublicacion() {
        imagenPendiente = null;

        View form = LayoutInflater.from(getContext()).inflate(R.layout.dialog_nueva_publicacion, null);
        TextInputEditText etTitulo = form.findViewById(R.id.etPubTitulo);
        TextInputEditText etCont = form.findViewById(R.id.etPubContenido);
        View btnImg = form.findViewById(R.id.btnElegirImagen);
        dialogImagePreview = form.findViewById(R.id.ivPreviewImagen);

        btnImg.setOnClickListener(v -> solicitarPermisoYAbrirSelector());

        dialogPublicar = new MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.muro_nueva_publicacion)
            .setView(form)
            .setPositiveButton(R.string.btn_publicar, (d, w) -> {})
            .setNegativeButton(R.string.btn_cancelar, null)
            .create();

        dialogPublicar.setOnShowListener(di -> {
            dialogPublicar.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String titulo = etTitulo.getText() != null ? etTitulo.getText().toString().trim() : "";
                String cont = etCont.getText() != null ? etCont.getText().toString().trim() : "";
                if (cont.isEmpty() && (imagenPendiente == null || imagenPendiente.length == 0)) {
                    Toast.makeText(getContext(), R.string.muro_pub_vacio, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (titulo.isEmpty()) titulo = getString(R.string.muro_pub_sin_titulo);

                int idUh = resolverIdUsuariosHuertasParaPublicar();
                if (idUh < 0) {
                    Toast.makeText(getContext(), R.string.muro_no_miembro, Toast.LENGTH_SHORT).show();
                    return;
                }

                String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String correo = sesionDao.obtenerCorreo();
                publicacionDao.insertar(titulo, cont, fecha, idUh, imagenPendiente, correo);
                dialogPublicar.dismiss();
                refrescarLista();
                Toast.makeText(getContext(), R.string.muro_pub_guardada, Toast.LENGTH_SHORT).show();
            });
        });

        dialogPublicar.show();
    }

    private void actualizarPreviewDialog() {
        if (dialogImagePreview == null) return;
        if (imagenPendiente != null && imagenPendiente.length > 0) {
            dialogImagePreview.setVisibility(View.VISIBLE);
            dialogImagePreview.setImageBitmap(BitmapFactory.decodeByteArray(
                imagenPendiente, 0, imagenPendiente.length));
        } else {
            dialogImagePreview.setVisibility(View.GONE);
        }
    }

    private void solicitarPermisoYAbrirSelector() {
        despuesDePermiso = this::abrirSelectorImagen;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                solicitudPermisoLectura.launch(Manifest.permission.READ_MEDIA_IMAGES);
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                solicitudPermisoLectura.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }
        }
        abrirSelectorImagen();
    }

    private void abrirSelectorImagen() {
        pickImagen.launch(new PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build());
    }

    /** Evita crash al llamar a onBackPressed durante la transacción del fragment. */
    private void cerrarFragmentPorError(@NonNull View anchor) {
        anchor.post(() -> {
            if (!isAdded()) {
                return;
            }
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            } else {
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (toolbarPrincipal != null) {
            toolbarPrincipal.setVisibility(View.VISIBLE);
        }
        if (getActivity() instanceof AppCompatActivity) {
            View tb = requireActivity().findViewById(R.id.toolbar);
            if (tb instanceof androidx.appcompat.widget.Toolbar) {
                ((AppCompatActivity) requireActivity()).setSupportActionBar(
                    (androidx.appcompat.widget.Toolbar) tb);
            }
        }
        super.onDestroyView();
    }
}
