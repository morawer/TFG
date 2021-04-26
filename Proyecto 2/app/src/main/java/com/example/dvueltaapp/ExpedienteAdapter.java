package com.example.dvueltaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExpedienteAdapter extends ArrayAdapter<Expedientes> {

    public ExpedienteAdapter(Context context,List<Expedientes> expediente) {
        super(context, 0, expediente);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listaPersonalizada = convertView;

        if (listaPersonalizada==null){
            listaPersonalizada = LayoutInflater.from(getContext()).inflate(R.layout.item_expediente, parent, false);
        }

        Expedientes expediente = getItem(position);

        TextView idExp = (TextView) listaPersonalizada.findViewById(R.id.expedienteId);
        TextView fechaExp = (TextView) listaPersonalizada.findViewById(R.id.expedienteFecha);
        TextView estadoExp = (TextView) listaPersonalizada.findViewById(R.id.expedienteEstado);
        TextView importeExp = (TextView) listaPersonalizada.findViewById(R.id.expedienteImporte);

        String idExpediente = expediente.getNroExp();
        idExp.setText(idExpediente);

        String fechaExpediente = expediente.getFechaExp();
        fechaExp.setText(fechaExpediente);

        String estadoExpediente = expediente.getEstadoExp();
        estadoExp.setText(estadoExpediente);

        String importeExpediente = expediente.getImporte();
        importeExp.setText(importeExpediente);

        return listaPersonalizada;
    }
}