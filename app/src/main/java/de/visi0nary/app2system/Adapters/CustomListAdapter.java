package de.visi0nary.app2system.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import de.visi0nary.app2system.MainActivity;
import de.visi0nary.app2system.Model.App;
import de.visi0nary.app2system.R;

/**
 * Created by visi0nary on 13.05.15.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    private ArrayList<App> apps;
    private MainActivity context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.text = (TextView) itemView.findViewById(R.id.label);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public CustomListAdapter(ArrayList<App> apps, MainActivity context) {
        this.apps = apps;
        this.context = context;
    }

    public void refreshItems(ArrayList<App> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    public void addItem(int position, App item) {
        apps.add(position, item);
        notifyItemInserted(position);
    }

    public void removeItem(App item) {
        int position = apps.indexOf(item);
        apps.remove(item);
        notifyItemRemoved(position);
    }

    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.singleentrylayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomListAdapter.ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = apps.get(position).getHumanReadableName();
        final App app = apps.get(position);
        viewHolder.text.setText(name);
        viewHolder.icon.setImageDrawable((apps.get(position).getIcon()));
        viewHolder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup(app);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }


    private void moveApp(App app) {
        //method that moves the app with a simple shell command (mv)
        if(context.isRootInitialized()) {
            BufferedWriter writer = context.getWriter();
            try {
                // remount system as read/write
                writer.write("mount -o remount,rw /system");
                writer.newLine();
                writer.flush();
                // move the app
                String moveCommand = determineMoveCommand(app);
                Log.i("output ", moveCommand);
                writer.write(moveCommand);
                writer.newLine();
                writer.flush();
                // and remount system as read only again
                writer.write("mount -o remount,ro /system");
                writer.newLine();
                writer.flush();
                removeItem(app);
                Toast.makeText(context, app.getHumanReadableName() + " has successfully been moved!", Toast.LENGTH_SHORT).show();
                //TODO inplement undo method
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String determineMoveCommand(App app) {
        // this method returns the move command (busybox mv *source* *destination*)
        StringBuilder finalCommandBuilder = new StringBuilder("busybox mv ");
        String path = new String(app.getPath());
        // thanks to Markus Heider for the idea of using split instead of a regex
        String[] splittedPath = path.split("/");
        StringBuilder pathBuilder = new StringBuilder();
        for (int i = 0; i < splittedPath.length - 1; i++) {
            pathBuilder.append(splittedPath[i] + "/");
            finalCommandBuilder.append(splittedPath[i] + "/");
        }
        String targetPath;
        if (app.isSystemApp()) {
            targetPath = pathBuilder.toString().replace("system", "data");
        } else {
            targetPath = pathBuilder.toString().replace("data", "system");
        }
        finalCommandBuilder.append(" " + targetPath);

        return finalCommandBuilder.toString();
    }


        public void createPopup(App app) {

            final App tempApp = app;
            int isUserApp = app.isSystemApp() ? 1 : 0;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            switch (isUserApp) {
                //it's a system app
                case 0:
                    alertDialogBuilder.setMessage(R.string.txt_alertdialog_move_to_data);
                    alertDialogBuilder.setTitle(R.string.txt_alertdialog_headline);
                    alertDialogBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user clicked ok
                            moveApp(tempApp);
                        }
                    });

                    alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user canceled action
                        }
                    });
                    break;
                //it's a user app
                case 1:
                    alertDialogBuilder.setMessage(R.string.txt_alertdialog_move_to_system);
                    alertDialogBuilder.setTitle(R.string.txt_alertdialog_headline);
                    alertDialogBuilder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user clicked ok
                            moveApp(tempApp);
                        }
                    });

                    alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user canceled action
                        }
                    });
                    break;
                //something went wrong (should never happen)
                default:
                    alertDialogBuilder.setMessage(R.string.txt_error_creating_dialog);
                    alertDialogBuilder.setTitle(R.string.txt_error_creating_dialog_headline);
                    alertDialogBuilder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //user canceled action
                        }
                    });

            }
            alertDialogBuilder.create().show();
        }


    //TODO: implement adapter sorting logic
}
