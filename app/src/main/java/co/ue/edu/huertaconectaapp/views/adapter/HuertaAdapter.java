package co.ue.edu.huertaconectaapp.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Set;

import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.model.Huerta;

public class HuertaAdapter extends RecyclerView.Adapter<HuertaAdapter.HuertaViewHolder> {

    public interface OnHuertaClickListener {
        void onHuertaClick(Huerta huerta);
    }

    private final List<Huerta> huertas;
    private final Set<Integer> idsMiembro;
    private final OnHuertaClickListener listener;

    public HuertaAdapter(List<Huerta> huertas, Set<Integer> idsMiembro, OnHuertaClickListener listener) {
        this.huertas = huertas;
        this.idsMiembro = idsMiembro;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HuertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_huerta, parent, false);
        return new HuertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HuertaViewHolder holder, int position) {
        Huerta huerta = huertas.get(position);
        holder.tvNombre.setText(huerta.getNombreHuerta());
        holder.tvDireccion.setText(huerta.getDireccionHuerta() != null ? huerta.getDireccionHuerta() : "Sin dirección");
        holder.tvDescripcion.setText(huerta.getDescripcion() != null ? huerta.getDescripcion() : "");

        boolean esMiembro = idsMiembro != null && idsMiembro.contains(huerta.getIdHuerta());
        holder.tvMiembro.setVisibility(esMiembro ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onHuertaClick(huerta));
    }

    @Override
    public int getItemCount() { return huertas.size(); }

    static class HuertaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion, tvDescripcion, tvMiembro;

        HuertaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre      = itemView.findViewById(R.id.tvItemNombreHuerta);
            tvDireccion   = itemView.findViewById(R.id.tvItemDireccion);
            tvDescripcion = itemView.findViewById(R.id.tvItemDescripcion);
            tvMiembro     = itemView.findViewById(R.id.tvItemMiembro);
        }
    }
}
