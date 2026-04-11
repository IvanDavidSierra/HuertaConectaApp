package co.ue.edu.huertaconectaapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.ue.edu.huertaconectaapp.MainActivity;
import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.controller.HuertaController;
import co.ue.edu.huertaconectaapp.model.HuertaRequest;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.HuertaDao;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;
import co.ue.edu.huertaconectaapp.model.db.dao.UsuarioHuertaDao;

public class CreateHuertaFragment extends Fragment {

    private EditText etNombreHuerta, etDireccionHuerta, etDescripcionHuerta;
    private Button btnGuardar, btnCancelar;
    private ProgressBar progressBar;

    private HuertaController huertaController;
    private HuertaDao huertaDao;
    private UsuarioHuertaDao usuarioHuertaDao;
    private SesionDao sesionDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_huerta, container, false);
        initObjects(view);
        return view;
    }

    private void initObjects(View view) {
        etNombreHuerta      = view.findViewById(R.id.etNombreHuerta);
        etDireccionHuerta   = view.findViewById(R.id.etDireccionHuerta);
        etDescripcionHuerta = view.findViewById(R.id.etDescripcionHuerta);
        btnGuardar          = view.findViewById(R.id.btnGuardarHuerta);
        btnCancelar         = view.findViewById(R.id.btnCancelarHuerta);
        progressBar         = view.findViewById(R.id.progressBarHuerta);

        DatabaseHelper db = DatabaseHelper.getInstance(requireContext());
        huertaController  = new HuertaController();
        huertaDao         = new HuertaDao(db);
        usuarioHuertaDao  = new UsuarioHuertaDao(db);
        sesionDao         = new SesionDao(db);

        btnGuardar.setOnClickListener(v -> guardarHuerta());
        btnCancelar.setOnClickListener(v ->
            ((MainActivity) requireActivity()).loadFragment(new HomeFragment())
        );
    }

    private void guardarHuerta() {
        String nombre      = etNombreHuerta.getText().toString().trim();
        String direccion   = etDireccionHuerta.getText().toString().trim();
        String descripcion = etDescripcionHuerta.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombreHuerta.setError("El nombre es obligatorio");
            etNombreHuerta.requestFocus();
            return;
        }
        if (nombre.length() < 10) {
            etNombreHuerta.setError("El nombre debe tener al menos 10 caracteres");
            etNombreHuerta.requestFocus();
            return;
        }
        if (descripcion.length() < 20) {
            etDescripcionHuerta.setError("La descripción debe tener al menos 20 caracteres");
            etDescripcionHuerta.requestFocus();
            return;
        }

        setLoading(true);

        String fechaCreacion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        HuertaRequest request = new HuertaRequest(nombre, descripcion, direccion, fechaCreacion);

        huertaController.crearHuerta(request, new HuertaController.HuertaListener() {
            @Override
            public void onSuccess(int huertaId) {
                // Guardar en SQLite local
                huertaDao.insertar(nombre, direccion, descripcion, fechaCreacion);

                // Vincular usuario con la huerta en la API
                int idUsuario = sesionDao.obtenerIdUsuario();
                String fechaVinculacion = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                huertaController.vincularUsuarioHuerta(idUsuario, huertaId, fechaVinculacion,
                    new HuertaController.VinculacionListener() {
                        @Override
                        public void onSuccess(int idUsuariosHuertas) {
                            // Guardar vinculación en SQLite local
                            usuarioHuertaDao.insertar(idUsuario, huertaId, fechaVinculacion);
                            requireActivity().runOnUiThread(() -> {
                                setLoading(false);
                                Toast.makeText(getContext(),
                                    "¡Huerta \"" + nombre + "\" creada exitosamente!",
                                    Toast.LENGTH_SHORT).show();
                                ((MainActivity) requireActivity()).loadFragment(new HomeFragment());
                            });
                        }

                        @Override
                        public void onError(String message) {
                            requireActivity().runOnUiThread(() -> {
                                setLoading(false);
                                Toast.makeText(getContext(),
                                    "Huerta creada pero no se pudo vincular: " + message,
                                    Toast.LENGTH_LONG).show();
                                ((MainActivity) requireActivity()).loadFragment(new HomeFragment());
                            });
                        }
                    });
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(getContext(), "Error al crear huerta: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnGuardar.setEnabled(!loading);
        btnCancelar.setEnabled(!loading);
    }
}
