package co.ue.edu.huertaconectaapp.views.adapter;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.model.Publicacion;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.VH> {

    private final List<Publicacion> items = new ArrayList<>();

    public void setItems(List<Publicacion> nuevos) {
        items.clear();
        if (nuevos != null) items.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_publicacion, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Publicacion p = items.get(position);
        String autor = p.getAutorCorreo();
        h.tvAutor.setText(TextUtils.isEmpty(autor) ? h.itemView.getContext().getString(R.string.muro_autor_desconocido) : autor);

        String fecha = p.getFechaPost();
        h.tvFecha.setText(fecha != null && fecha.length() >= 10 ? fecha.substring(0, 10) : (fecha != null ? fecha : ""));

        String titulo = p.getTitulo();
        if (!TextUtils.isEmpty(titulo)) {
            h.tvTitulo.setVisibility(View.VISIBLE);
            h.tvTitulo.setText(titulo);
        } else {
            h.tvTitulo.setVisibility(View.GONE);
        }

        String cont = p.getContenido();
        if (TextUtils.isEmpty(cont)) {
            h.tvContenido.setVisibility(View.GONE);
        } else {
            h.tvContenido.setVisibility(View.VISIBLE);
            h.tvContenido.setText(cont);
        }

        byte[] blob = p.getImagenBlob();
        if (blob != null && blob.length > 0) {
            h.ivImagen.setVisibility(View.VISIBLE);
            h.ivImagen.setImageBitmap(BitmapFactory.decodeByteArray(blob, 0, blob.length));
        } else {
            h.ivImagen.setVisibility(View.GONE);
            h.ivImagen.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvAutor, tvFecha, tvTitulo, tvContenido;
        final ImageView ivImagen;

        VH(@NonNull View itemView) {
            super(itemView);
            tvAutor = itemView.findViewById(R.id.tvPubAutor);
            tvFecha = itemView.findViewById(R.id.tvPubFecha);
            tvTitulo = itemView.findViewById(R.id.tvPubTitulo);
            tvContenido = itemView.findViewById(R.id.tvPubContenido);
            ivImagen = itemView.findViewById(R.id.ivPubImagen);
        }
    }
}
