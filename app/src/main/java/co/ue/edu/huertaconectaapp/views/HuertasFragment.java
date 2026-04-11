package co.ue.edu.huertaconectaapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.ue.edu.huertaconectaapp.MainActivity;
import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.model.Huerta;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaDetalle;
import co.ue.edu.huertaconectaapp.model.remote.ClientRetrofit;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;
import co.ue.edu.huertaconectaapp.views.adapter.HuertaAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HuertasFragment extends Fragment {

    private RecyclerView rvHuertas;
    private ProgressBar progressBar;
    private TextView tvSinHuertas;

    private final List<Huerta> listaHuertas = new ArrayList<>();
    private final Set<Integer> idsMiembro = new HashSet<>();
    private HuertaAdapter adapter;
    private SesionDao sesionDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_huertas, container, false);
        initObjects(view);
        cargarMisHuertas();
        return view;
    }

    private void initObjects(View view) {
        rvHuertas    = view.findViewById(R.id.rvHuertas);
        progressBar  = view.findViewById(R.id.progressBarHuertas);
        tvSinHuertas = view.findViewById(R.id.tvSinHuertas);

        sesionDao = new SesionDao(DatabaseHelper.getInstance(requireContext()));

        adapter = new HuertaAdapter(listaHuertas, idsMiembro, huerta -> {
            MainActivity act = (MainActivity) requireActivity();
            if (idsMiembro.contains(huerta.getIdHuerta())) {
                act.pushFragment(HuertaPublicacionesFragment.newInstance(huerta, true));
            } else {
                act.pushFragment(HuertaDetalleFragment.newInstance(huerta));
            }
        });

        rvHuertas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHuertas.setAdapter(adapter);
    }

    // Primero carga las huertas del usuario para marcarlas como "Miembro"
    private void cargarMisHuertas() {
        setLoading(true);
        int idUsuario = sesionDao.obtenerIdUsuario();
        ClientRetrofit.getService().getMisHuertas(idUsuario).enqueue(new Callback<List<UsuarioHuertaDetalle>>() {
            @Override
            public void onResponse(Call<List<UsuarioHuertaDetalle>> call, Response<List<UsuarioHuertaDetalle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (UsuarioHuertaDetalle uh : response.body()) {
                        if (uh.getHuerta() != null) {
                            idsMiembro.add(uh.getHuerta().getIdHuerta());
                        }
                    }
                }
                cargarTodasLasHuertas();
            }

            @Override
            public void onFailure(Call<List<UsuarioHuertaDetalle>> call, Throwable t) {
                cargarTodasLasHuertas();
            }
        });
    }

    private void cargarTodasLasHuertas() {
        ClientRetrofit.getService().getHuertas().enqueue(new Callback<List<Huerta>>() {
            @Override
            public void onResponse(Call<List<Huerta>> call, Response<List<Huerta>> response) {
                if (!isAdded()) return;
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    listaHuertas.clear();
                    listaHuertas.addAll(response.body());
                    requireActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        tvSinHuertas.setVisibility(listaHuertas.isEmpty() ? View.VISIBLE : View.GONE);
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Huerta>> call, Throwable t) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setLoading(false);
                    tvSinHuertas.setVisibility(View.VISIBLE);
                    tvSinHuertas.setText("Error al cargar huertas. Verifica tu conexión.");
                });
            }
        });
    }

    private void setLoading(boolean loading) {
        if (!isAdded()) return;
        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            rvHuertas.setVisibility(loading ? View.GONE : View.VISIBLE);
        });
    }
}
