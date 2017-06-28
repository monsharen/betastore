package com.lionsinvests.betastore;

import java.util.List;

import com.lionsinvests.betastore.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationAdapter extends ArrayAdapter<Application> {

    private ApplicationManager applicationManager;

    ApplicationAdapter(Context context, List<Application> applications) {
        super(context, 0, applications);
        this.applicationManager = new ApplicationManager(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Application application = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_application, parent, false);
        }

        final boolean installed = applicationManager.isInstalled(application);

        TextView appName = (TextView) convertView.findViewById(R.id.appName);
        appName.setText(application.getName());

        ImageView icon = (ImageView) convertView.findViewById(R.id.downloadOrDelete);
        icon.setImageResource(getInstallButtonImage(installed));

        icon = (ImageView) convertView.findViewById(R.id.appImage);
        if (application.getIcon() != null) {
            icon.setImageDrawable(application.getIcon());
        } else {
            icon.setImageResource(R.mipmap.placeholder);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!installed) {
                    if (applicationManager.download(application)) {
                        ToastUtil.showToast(getContext(), "Failed to start download");
                    }
                } else {
                    applicationManager.uninstall(application);
                }
            }
        });

        return convertView;
    }

    private int getInstallButtonImage(boolean installed) {
        return installed ? R.mipmap.ic_delete_forever_white_24dp : R.mipmap.ic_cloud_download_white_24dp;
    }


}
