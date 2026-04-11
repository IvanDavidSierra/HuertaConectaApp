package co.ue.edu.huertaconectaapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import co.ue.edu.huertaconectaapp.MainActivity;
import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.model.DashboardResponse;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;
import co.ue.edu.huertaconectaapp.model.remote.ClientRetrofit;
import co.ue.edu.huertaconectaapp.views.HuertasFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView tvNombreUsuario, tvCorreoUsuario, tvBienvenida;
    private TextView tvCountHuertas, tvCountCultivos, tvCountPublicaciones;
    private Button btnAccesoHuertas, btnAccesoServicios;
    private SesionDao sesionDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initObjects(view);
        cargarDatosUsuario();
        cargarDashboard();
        return view;
    }

    private void initObjects(View view) {
        tvBienvenida           = view.findViewById(R.id.tvBienvenida);
        tvNombreUsuario        = view.findViewById(R.id.tvNombreUsuario);
        tvCorreoUsuario        = view.findViewById(R.id.tvCorreoUsuario);
        tvCountHuertas         = view.findViewById(R.id.tvCountHuertas);
        tvCountCultivos        = view.findViewById(R.id.tvCountCultivos);
        tvCountPublicaciones   = view.findViewById(R.id.tvCountPublicaciones);
        btnAccesoHuertas       = view.findViewById(R.id.btnAccesoHuertas);
        btnAccesoServicios     = view.findViewById(R.id.btnAccesoServicios);

        sesionDao = new SesionDao(DatabaseHelper.getInstance(requireContext()));

        btnAccesoHuertas.setOnClickListener(v ->
            ((MainActivity) requireActivity()).loadFragment(new HuertasFragment())
        );
        btnAccesoServicios.setOnClickListener(v ->
            ((MainActivity) requireActivity()).loadFragment(new ServicesFragment())
        );
    }

    private void cargarDatosUsuario() {
        String correo = sesionDao.obtenerCorreo();
        if (correo != null && !correo.isEmpty()) {
            String nombre = correo.contains("@")
                ? correo.substring(0, correo.indexOf("@"))
                : correo;
            String nombreCapitalizado = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
            tvBienvenida.setText("¡Bienvenido de vuelta,");
            tvNombreUsuario.setText(nombreCapitalizado + "!");
            tvCorreoUsuario.setText(correo);
        }
    }

    private void cargarDashboard() {
        ClientRetrofit.getService().getDashboardCounts().enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                if (response.isSuccessful() && response.body() != null && isAdded()) {
                    DashboardResponse data = response.body();
                    requireActivity().runOnUiThread(() -> {
                        tvCountHuertas.setText(String.valueOf(data.getHuertas()));
                        tvCountCultivos.setText(String.valueOf(data.getCultivos()));
                        tvCountPublicaciones.setText(String.valueOf(data.getPublicaciones()));
                    });
                }
            }

            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        tvCountHuertas.setText("—");
                        tvCountCultivos.setText("—");
                        tvCountPublicaciones.setText("—");
                    });
                }
            }
        });
    }
}
