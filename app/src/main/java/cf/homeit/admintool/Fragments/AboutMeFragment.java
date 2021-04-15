package cf.homeit.admintool.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shashank.sony.fancyaboutpagelib.FancyAboutPage;

import cf.homeit.admintool.BuildConfig;
import cf.homeit.admintool.R;

public class AboutMeFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_about, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FancyAboutPage fancyAboutPage = view.findViewById(R.id.fancyaboutpage);
        fancyAboutPage.setCoverTintColor(Color.BLUE);  //Optional
        fancyAboutPage.setCover(R.drawable.cover_img); //Pass your cover image
        fancyAboutPage.setName("Denis I. Rykov");
        fancyAboutPage.setDescription("Android App, Game, Web and Software Developer.");
        fancyAboutPage.setAppIcon(R.mipmap.ic_launcher); //Pass your app icon image
        fancyAboutPage.setAppName(getString(R.string.app_name));
        fancyAboutPage.setVersionNameAsAppSubTitle(BuildConfig.VERSION_NAME);
        fancyAboutPage.setAppDescription("Cake Pop Icon Pack is an icon pack which follows Google's Material Design language.\n\n" +
                "This icon pack uses the material design color palette given by google. Every icon is handcrafted with attention to the smallest details!\n\n");
        fancyAboutPage.addEmailLink("drycov@gmail.com");     //Add your email id
        fancyAboutPage.addGitHubLink("https://github.com/Vargar929");
    }
}
