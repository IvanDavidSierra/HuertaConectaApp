package co.ue.edu.huertaconectaapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.ue.edu.huertaconectaapp.MainActivity;
import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.controller.HuertaController;
import co.ue.edu.huertaconectaapp.model.Huerta;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;
import co.ue.edu.huertaconectaapp.model.db.dao.UsuarioHuertaDao;

public class HuertaDetalleFragment extends Fragment {

    private static final String ARG_HUERTA = "huerta";

    private TextView tvNombre, tvDireccion, tvDescripcion, tvFecha, tvMiembro;
    private Button btnUnirse;
    private MaterialButton btnVerMuroLectura;
    private ProgressBar progressBar;

    private Huerta huerta;
    private SesionDao sesionDao;
    private UsuarioHuertaDao usuarioHuertaDao;
    private HuertaController huertaController;

    public static HuertaDetalleFragment newInstance(Huerta huerta) {
        HuertaDetalleFragment fragment = new HuertaDetalleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_HUERTA, huerta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_huerta_detalle, container, false);

        if (getArguments() != null) {
            huerta = (Huerta) getArguments().getSerializable(ARG_HUERTA);
        }

        initObjects(view);
        poblarDatos();
        verificarMembresia();
        return view;
    }

    private void initObjects(View view) {
        tvNombre      = view.findViewById(R.id.tvDetalleNombre);
        tvDireccion   = view.findViewById(R.id.tvDetalleDireccion);
        tvDescripcion = view.findViewById(R.id.tvDetalleDescripcion);
        tvFecha       = view.findViewById(R.id.tvDetalleFecha);
        tvMiembro     = view.findViewById(R.id.tvDetalleMiembro);
        btnUnirse          = view.findViewById(R.id.btnUnirse);
        btnVerMuroLectura  = view.findViewById(R.id.btnVerMuroLectura);
        progressBar        = view.findViewById(R.id.progressBarDetalle);

        DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
        sesionDao        = new SesionDao(db);
        usuarioHuertaDao = new UsuarioHuertaDao(db);
        huertaController = new HuertaController();
    }

    private void poblarDatos() {
        if (huerta == null) return;
        tvNombre.setText(huerta.getNombreHuerta());
        tvDireccion.setText(huerta.getDireccionHuerta() != null ? huerta.getDireccionHuerta() : "Sin dirección");
        tvDescripcion.setText(huerta.getDescripcion() != null ? huerta.getDescripcion() : "Sin descripción");

        String fecha = huerta.getFechaCreacion();
        if (fecha != null && fecha.length() >= 10) {
            tvFecha.setText(fecha.substring(0, 10));
        } else {
            tvFecha.setText("—");
        }
    }

    private void verificarMembresia() {
        if (huerta == null) {
            return;
        }
        // Verificar en SQLite local si ya es miembro
        android.database.Cursor cursor = usuarioHuertaDao.obtenerHuertasPorUsuario(
            sesionDao.obtenerIdUsuario()
        );
        boolean esMiembro = false;
        while (cursor.moveToNext()) {
            int idHuerta = cursor.getInt(cursor.getColumnIndexOrThrow("id_huerta"));
            if (idHuerta == huerta.getIdHuerta()) {
                esMiembro = true;
                break;
            }
        }
        cursor.close();
        actualizarEstadoMiembro(esMiembro);
    }

    private void actualizarEstadoMiembro(boolean esMiembro) {
        if (esMiembro) {
            tvMiembro.setVisibility(View.VISIBLE);
            btnVerMuroLectura.setVisibility(View.GONE);
            btnUnirse.setText(R.string.detalle_ir_al_muro);
            btnUnirse.setOnClickListener(v ->
                ((MainActivity) requireActivity()).pushFragment(
                    HuertaPublicacionesFragment.newInstance(huerta, false))
            );
        } else {
            tvMiembro.setVisibility(View.GONE);
            btnVerMuroLectura.setVisibility(View.VISIBLE);
            btnVerMuroLectura.setOnClickListener(v ->
                ((MainActivity) requireActivity()).pushFragment(
                    HuertaPublicacionesFragment.newInstance(huerta, false, true))
            );
            btnUnirse.setText(R.string.detalle_unirse_huerta);
            btnUnirse.setOnClickListener(v -> unirseAHuerta());
        }
    }

    private void unirseAHuerta() {
        setLoading(true);
        int idUsuario = sesionDao.obtenerIdUsuario();
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        huertaController.vincularUsuarioHuerta(idUsuario, huerta.getIdHuerta(), fecha,
            new HuertaController.VinculacionListener() {
                @Override
                public void onSuccess(int idUsuariosHuertas) {
                    usuarioHuertaDao.insertarConReferencias(
                        idUsuario, huerta.getIdHuerta(), fecha, huerta);
                    requireActivity().runOnUiThread(() -> {
                        setLoading(false);
                        Toast.makeText(getContext(),
                            "¡Te uniste a \"" + huerta.getNombreHuerta() + "\"!", Toast.LENGTH_SHORT).show();
                        actualizarEstadoMiembro(true);
                    });
                }

                @Override
                public void onError(String message) {
                    requireActivity().runOnUiThread(() -> {
                        setLoading(false);
                        Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                    });
                }
            });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnUnirse.setEnabled(!loading);
        btnVerMuroLectura.setEnabled(!loading);
    }
}
