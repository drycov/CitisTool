package cf.homeit.admintool.ViewHolder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import cf.homeit.admintool.DataModels.Note;
import cf.homeit.admintool.DataModels.Vlan;
import cf.homeit.admintool.R;

public class VlanViewHolder extends RecyclerView.ViewHolder{
    public final MaterialTextView vlanIditem, vlanNameIem;

    public VlanViewHolder(View itemView){
        super(itemView);
        vlanIditem = itemView.findViewById(R.id.vlanIditem);
        vlanNameIem = itemView.findViewById(R.id.vlanNameIem);
//        buttonViewOption = itemView.findViewById(R.id.textViewOptionsVlan);

    }
    public void bindToModel(Vlan model) {
        vlanIditem.setText(model.vlanId);
        vlanNameIem.setText(model.vlanName);
    }
}
