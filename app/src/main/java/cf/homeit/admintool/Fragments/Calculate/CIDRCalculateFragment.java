package cf.homeit.admintool.Fragments.Calculate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import cf.homeit.admintool.R;
import cf.homeit.admintool.ExtendsClases.inputFilterMinMax;

public class CIDRCalculateFragment extends Fragment {
    private MaterialTextView scrollable,outputResult;
    private EditText octet4Data, octet3Data, octet2Data, octet1Data, subOctet4Data, subOctet3Data, subOctet2Data, subOctet1Data;
    private TextView binIP, binMask, netAddr, broadcAddr, fUsableAddr, lUsableAddr, ipCls;

//    outputResult.setText("Binary IP" + "\n" + binaryValue4 + "." + binaryValue3  + "." + binaryValue2  + "." + binaryValue1
//                    + "\n\n" + "Binary Mask" + "\n" + binaryOct4Result + "." + binaryOct3Result + "." + binaryOct2Result + "." + binaryOct1Result
//                    + "\n" + "\n" + "Network Address" + "\n" + network4Result + "." + network3Result + "." + network2Result + "." + network1Result +
//            "\n\n" + "Broadcast Address" + "\n" + network4Result + "." + broadcastResult3 + "." + broadcastResult2+ "." + broadcastResult1 + "\n\n" +
//            "First Usable Address" + "\n"  + network4Result + "." + network3Result + "." + network2Result + "." + firstUsable + "\n\n" +
//            "Last Usable Address" + "\n" + network4Result + "." + broadcastResult3 + "." + broadcastResult2 + "." + lastUsable + "\n\n" +
//            "Ip Class" + "\n" + ipClass);
    //IP Octet values
    private int ipValue4, ipValue3, ipValue2, ipValue1;

    //Subnet Octet Values
    private int subnetValue4, subnetValue3, subnetValue2, subnetValue1;

    //Declared Integers for Network Address ANDing
    private int network4Result, network3Result, network2Result, network1Result;

    //Declared Integer for First Usable Address
    private int firstUsable;

    //broadcast Integer
    private int broadcastResult3, broadcastResult2, broadcastResult1;

    //Declared Integer for Last Usable Address
    private int lastUsable;

    //Strings for Binary converted IP Octets
    private String binaryValue4, binaryValue2, binaryValue3, binaryValue1;

    //Strings for Binary converted Subnet Octets
    private String binaryOct4Result, binaryOct3Result, binaryOct2Result, binaryOct1Result;

    //Declared String for the ip Class
    private String ipClass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_calc_cidr, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        onViewInit(view);
        view.findViewById(R.id.cidrCalc).setOnClickListener(v -> {



            if(octet4Data.getText().toString().equals("") || subOctet4Data.getText().toString().equals("")||
                    octet3Data.getText().toString().equals("")|| subOctet3Data.getText().toString().equals("")||
                    octet2Data.getText().toString().equals("")|| subOctet2Data.getText().toString().equals("")||
                    octet1Data.getText().toString().equals("")|| subOctet1Data.getText().toString().equals("")) {
                Toast.makeText(requireActivity().getApplicationContext(), "You must enter numbers in all Fields ", Toast.LENGTH_SHORT).show();
                return;
            }

            //Passes ip octet Integers into String values
            ipValue4 = Integer.parseInt(octet4Data.getText().toString());
            ipValue3 = Integer.parseInt(octet3Data.getText().toString());
            ipValue2 = Integer.parseInt(octet2Data.getText().toString());
            ipValue1 = Integer.parseInt(octet1Data.getText().toString());

            //Passes subnet octet Integers into String values
            subnetValue4 = Integer.parseInt(subOctet4Data.getText().toString());
            subnetValue3 = Integer.parseInt(subOctet3Data.getText().toString());
            subnetValue2 = Integer.parseInt(subOctet2Data.getText().toString());
            subnetValue1 = Integer.parseInt(subOctet1Data.getText().toString());

            //Converts ip value to Binary
            binaryValue4 = String.format("%8s", Integer.toBinaryString(ipValue4)).replace(" ", "0");
            binaryValue3 = String.format("%8s", Integer.toBinaryString(ipValue3)).replace(" ", "0");
            binaryValue2 = String.format("%8s", Integer.toBinaryString(ipValue2)).replace(" ", "0");
            binaryValue1 = String.format("%8s", Integer.toBinaryString(ipValue1)).replace(" ", "0");

            //Converts subnet value to Binary
            binaryOct4Result = String.format("%8s", Integer.toBinaryString(subnetValue4)).replace(" ", "0");
            binaryOct3Result = String.format("%8s", Integer.toBinaryString(subnetValue3)).replace(" ", "0");
            binaryOct2Result = String.format("%8s", Integer.toBinaryString(subnetValue2)).replace(" ", "0");
            binaryOct1Result = String.format("%8s", Integer.toBinaryString(subnetValue1)).replace(" ", "0");

            //ANDing for Network Address
            network4Result = ipValue4 & subnetValue4;
            network3Result = ipValue3 & subnetValue3;
            network2Result = ipValue2 & subnetValue2;
            network1Result = ipValue1 & subnetValue1;

            //Broadcast Address
            String invertedSubnet1 = binaryOct1Result;
            String invertedSubnet2 = binaryOct2Result;
            String invertedSubnet3 = binaryOct3Result;

            invertedSubnet1 = invertedSubnet1.replaceAll("0", "x").replaceAll("1", "0").replaceAll("x", "1");
            invertedSubnet2 = invertedSubnet2.replaceAll("0", "x").replaceAll("1", "0").replaceAll("x", "1");
            invertedSubnet3 = invertedSubnet3.replaceAll("0", "x").replaceAll("1", "0").replaceAll("x", "1");

            int inverted1 = Integer.parseInt(invertedSubnet1, 2);
            int inverted2 = Integer.parseInt(invertedSubnet2, 2);
            int inverted3 = Integer.parseInt(invertedSubnet3, 2);

            if (subnetValue3 <= 254) {
                broadcastResult3 = ipValue3 | inverted3;
            } else
                broadcastResult3 = network3Result;

            if (subnetValue2 <=254) {
                broadcastResult2 = ipValue2 | inverted2;
            } else
                broadcastResult2 = network2Result;

            broadcastResult1 = ipValue1 | inverted1;


            //First Usable Address
            firstUsable = network1Result + 1;

            //Last Usable Address
            lastUsable = broadcastResult1 - 1;

            //Checking IP Class
            ipClass = "";

            if (network3Result <= 11111110) {
                ipClass = "A";
            } else if (network2Result <= 11111110){
                ipClass = "B";
            } else if (network1Result >= 0-0000000) {
                ipClass = "C";
            }
            //        private TextView binIP, binMask, netAddr, broadcAddr, fUsableAddr, lUsableAddr, ipCls;
            binIP.setText(binaryValue4 + "." + binaryValue3  + "." + binaryValue2  + "." + binaryValue1);
            binMask.setText(binaryOct4Result + "." + binaryOct3Result + "." + binaryOct2Result + "." + binaryOct1Result);
            netAddr.setText(network4Result + "." + network3Result + "." + network2Result + "." + network1Result);
            broadcAddr.setText(network4Result + "." + broadcastResult3 + "." + broadcastResult2+ "." + broadcastResult1);
            fUsableAddr.setText(network4Result + "." + network3Result + "." + network2Result + "." + firstUsable);
            lUsableAddr.setText(network4Result + "." + broadcastResult3 + "." + broadcastResult2 + "." + lastUsable);
            ipCls.setText(ipClass);
        });
    }


    @SuppressLint("CutPasteId")
    private void onViewInit(View view) {
//        octet4Data, octet3Data, octet2Data, octet1Data, subOctet4Data, subOctet3Data, subOctet2Data, subOctet1Data;
//        scrollable = view.findViewById(R.id.outputResultTextViewCIDR);

        octet4Data = view.findViewById(R.id.cidrIpVal4);
        octet3Data = view.findViewById(R.id.cidrIpVal3);
        octet2Data = view.findViewById(R.id.cidrIpVal2);
        octet1Data = view.findViewById(R.id.cidrIpVal1);
        subOctet4Data = view.findViewById(R.id.cidrMaskVal4);
        subOctet3Data = view.findViewById(R.id.cidrMaskVal3);
        subOctet2Data = view.findViewById(R.id.cidrMaskVal2);
        subOctet1Data = view.findViewById(R.id.cidrMaskVal1);
//        outputResult = view.findViewById(R.id.outputResultTextViewCIDR);
        binIP = view.findViewById(R.id.binaryIPResult);
        binMask = view.findViewById(R.id.binaryMaskResult);
        netAddr = view.findViewById(R.id.networkAddressCidrResult);
        broadcAddr = view.findViewById(R.id.BroadcastAddressCidrResult);
        fUsableAddr = view.findViewById(R.id.FUHCidrResult);
        lUsableAddr = view.findViewById(R.id.LastUHostCidrResult);
        ipCls = view.findViewById(R.id.NetClassCidrResult);
        octet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
        octet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
        octet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
        octet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
        subOctet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
        subOctet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
        subOctet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
        subOctet4Data.setFilters(new InputFilter[]{new inputFilterMinMax("0", "255")});
    }
}
