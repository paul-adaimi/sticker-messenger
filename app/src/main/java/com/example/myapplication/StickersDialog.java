package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class StickersDialog extends AppCompatDialogFragment {
    ArrayList<ImageView> stickers = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_stickers_dialog, null);

        builder.setView(view)
         .setTitle("Choose Sticker")
         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        addStickers(view);

        return builder.create();
    }

    private void addStickers(View view) {
        stickers.add(view.findViewById(R.id.sticker_1));
        stickers.add(view.findViewById(R.id.sticker_2));
        stickers.add(view.findViewById(R.id.sticker_3));

        for(ImageView imageView: stickers) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(ImageView imageView: stickers) {
                        imageView.setBackgroundColor(getResources().getColor(R.color.white));
                    }

                    view.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            });
        }
    }
}
