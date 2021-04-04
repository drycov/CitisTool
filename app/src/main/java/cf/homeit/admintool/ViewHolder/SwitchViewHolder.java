package cf.homeit.admintool.ViewHolder;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.lang.reflect.Field;

import cf.homeit.admintool.DataModels.Switch;
import cf.homeit.admintool.R;

import static cf.homeit.admintool.ExtendsClases.SupportVoids.showToast;

public class SwitchViewHolder extends RecyclerView.ViewHolder{
    public final MaterialTextView switchSNItem, switchSysNameItem, switchLocationItem, switchIpItem,buttonViewOption;

    @SuppressLint("RestrictedApi")
    public SwitchViewHolder(View itemView){
        super(itemView);
        switchSNItem = itemView.findViewById(R.id.switchSNItem);
        switchSysNameItem = itemView.findViewById(R.id.switchSysNameItem);
        switchLocationItem = itemView.findViewById(R.id.switchLocationItem);
        switchIpItem = itemView.findViewById(R.id.switchIpItem);
        buttonViewOption = itemView.findViewById(R.id.textViewOptionsSwitch);


    }
    public void bindToModel(Switch model) {
        switchSNItem.setText(model.switchSN);
        switchSysNameItem.setText(model.switchSysName);
        switchLocationItem.setText(model.switchLocation);
        switchIpItem.setText(model.switchIp);
    }
}
