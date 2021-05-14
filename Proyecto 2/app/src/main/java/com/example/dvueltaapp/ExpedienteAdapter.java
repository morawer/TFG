package com.example.dvueltaapp;

import android.annotation.SuppressLint;
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

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listaPersonalizada = convertView;

        if (listaPersonalizada==null){
            listaPersonalizada = LayoutInflater.from(getContext()).inflate(R.layout.item_expediente, parent, false);
        }

        Expedientes expediente = getItem(position);

        TextView idExp = listaPersonalizada.findViewById(R.id.expedienteId);
        TextView fechaExp = listaPersonalizada.findViewById(R.id.expedienteFecha);
        TextView estadoExp = listaPersonalizada.findViewById(R.id.expedienteEstado);
        TextView importeExp = listaPersonalizada.findViewById(R.id.expedienteImporte);

        String idExpediente = expediente.getNroExp();
        String[] expsPartes = idExpediente.split(",");
        int ultimoExps = expsPartes.length - 1;
        idExp.setText("Nº exp: " + expsPartes[ultimoExps]);

        String fechaExpediente = expediente.getFechaExp();
        fechaExp.setText("Fecha: " + fechaExpediente.substring(0, 10));

        String estadoExpediente = expediente.getEstadoExp();
        estadoExp.setText("Estado: " + estadoExpediente);

        String importeExpediente = expediente.getImporte();
        if (importeExpediente.equals(".00")) {
            importeExp.setText("Importe: 0 €");
        }else {
            importeExp.setText("Importe: " + importeExpediente + " €");
        }
        return listaPersonalizada;
    }
}