package co.ue.edu.huertaconectaapp.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;
import co.ue.edu.huertaconectaapp.model.remote.ClientRetrofit;
import co.ue.edu.huertaconectaapp.views.adapter.HuertaAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisHuertasFragment extends Fragment {

    private RecyclerView rvMisHuertas;
    private ProgressBar progressBar;
    private LinearLayout layoutSinHuertas;

    private final List<Huerta> misHuertas = new ArrayList<>();
    private final Set<Integer> idsMiembro = new HashSet<>();
    private HuertaAdapter adapter;
    private SesionDao sesionDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_huertas, container, false);
        initObjects(view);
        cargarMisHuertas();
        return view;
    }

    private void initObjects(View view) {
        rvMisHuertas    = view.findViewById(R.id.rvMisHuertas);
        progressBar     = view.findViewById(R.id.progressMisHuertas);
        layoutSinHuertas = view.findViewById(R.id.layoutSinMisHuertas);
        Button btnExplorar = view.findViewById(R.id.btnExplorarHuertas);

        sesionDao = new SesionDao(DatabaseHelper.getInstance(requireContext()));

        adapter = new HuertaAdapter(misHuertas, idsMiembro, huerta ->
            ((MainActivity) requireActivity()).pushFragment(
                HuertaPublicacionesFragment.newInstance(huerta, true))
        );

        rvMisHuertas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMisHuertas.setAdapter(adapter);

        btnExplorar.setOnClickListener(v ->
            ((MainActivity) requireActivity()).loadFragment(new HuertasFragment())
        );
    }

    private void cargarMisHuertas() {
        setLoading(true);
        int idUsuario = sesionDao.obtenerIdUsuario();

        ClientRetrofit.getService().getMisHuertas(idUsuario).enqueue(new Callback<List<UsuarioHuertaDetalle>>() {
            @Override
            public void onResponse(Call<List<UsuarioHuertaDetalle>> call, Response<List<UsuarioHuertaDetalle>> response) {
                if (!isAdded()) return;
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    misHuertas.clear();
                    for (UsuarioHuertaDetalle uh : response.body()) {
                        if (uh.getHuerta() != null) {
                            misHuertas.add(uh.getHuerta());
                            idsMiembro.add(uh.getHuerta().getIdHuerta());
                        }
                    }
                    requireActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        layoutSinHuertas.setVisibility(misHuertas.isEmpty() ? View.VISIBLE : View.GONE);
                        rvMisHuertas.setVisibility(misHuertas.isEmpty() ? View.GONE : View.VISIBLE);
                    });
                } else {
                    mostrarSinHuertas();
                }
            }

            @Override
            public void onFailure(Call<List<UsuarioHuertaDetalle>> call, Throwable t) {
                if (!isAdded()) return;
                requireActivity().runOnUiThread(() -> {
                    setLoading(false);
                    mostrarSinHuertas();
                });
            }
        });
    }

    private void mostrarSinHuertas() {
        layoutSinHuertas.setVisibility(View.VISIBLE);
        rvMisHuertas.setVisibility(View.GONE);
    }

    private void setLoading(boolean loading) {
        if (!isAdded()) return;
        requireActivity().runOnUiThread(() ->
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE)
        );
    }
}
