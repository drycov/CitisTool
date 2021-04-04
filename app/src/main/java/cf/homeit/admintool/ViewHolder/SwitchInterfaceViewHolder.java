package cf.homeit.admintool.ViewHolder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import cf.homeit.admintool.DataModels.Port;
import cf.homeit.admintool.R;

public class SwitchInterfaceViewHolder extends RecyclerView.ViewHolder{
    public final MaterialTextView portId;

    public SwitchInterfaceViewHolder(View itemView){
        super(itemView);
        portId = itemView.findViewById(R.id.portIdItem);
//        portType = itemView.findViewById(R.id.portTypeItem);
//        portDest = itemView.findViewById(R.id.portDestItem);
//        portStatus = itemView.findViewById(R.id.portStatusItem);
//        buttonViewOption = itemView.findViewById(R.id.textViewOptionsPort);
    }
    public void bindToModel(Port model) {
        portId.setText(model.portId);
//        portType.setText(model.portType);
//        portDest.setText(model.portDest);
//        portStatus.setText(model.portStatus);
    }
}
