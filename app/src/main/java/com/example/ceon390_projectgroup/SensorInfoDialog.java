package com.example.ceon390_projectgroup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SensorInfoDialog extends Dialog {

    String sensorName;
    String marketName;
    String gasDetection;
    String dangers;
    String moreInfo;

    /**
     * Constructor Method
     *
     * @param context
     * @param sensorName
     * @param marketName
     * @param gasDetection
     * @param dangers
     * @param moreInfo
     */
    public SensorInfoDialog(@NonNull Context context, String sensorName, String marketName, String gasDetection, String dangers, String moreInfo) {
        super(context);
        this.sensorName = sensorName;
        this.marketName = marketName;
        this.gasDetection = gasDetection;
        this.dangers = dangers;
        this.moreInfo = moreInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sensor_info);

        setCancelable(false);

        TextView tvSensorName = findViewById(R.id.tvSensorName);
        TextView tvMarketName = findViewById(R.id.tvMarketName);
        TextView tvGasDetection = findViewById(R.id.tvGasDetection);
        TextView tvDangers = findViewById(R.id.tvDangers);
        TextView tvMoreInfo = findViewById(R.id.tvMoreInfo);
        Button btnDone = findViewById(R.id.btnDone);

        // Setting info values in the views
        tvSensorName.setText(sensorName);
        tvMarketName.setText(marketName);
        tvGasDetection.setText(gasDetection);
        tvDangers.setText(dangers);
        tvMoreInfo.setText(moreInfo);

        // Done button to dismiss the dialog
        btnDone.setOnClickListener(v -> dismiss());
    }
}
