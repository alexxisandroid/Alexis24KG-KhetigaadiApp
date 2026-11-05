package com.khetigaadi.tractor.newTractor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.khetigaadi.AppConfig;
import com.khetigaadi.commons.utils.Product;
import com.khetigaadi.databinding.NewTractorPicturesBinding;
import com.khetigaadi.tractor.newTractor.model.NewTractorPictureResp;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NewTractorDetailsPicture extends Fragment {
    private final String TAG = getClass().getSimpleName();
    private FragmentActivity activity;
    private NewTractorPicturesBinding binding;
    private List<Product> urls = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = NewTractorPicturesBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }
    private void initView(List<Product> urls) {
        NewTractorDetailsPictureAdapter adapter = new NewTractorDetailsPictureAdapter(activity,urls);
        binding.viewPager.setAdapter(adapter);
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        NewTractorViewModel viewModel = new ViewModelProvider((NewTractorDetails)activity).get(NewTractorViewModel.class);
        viewModel.getPictureDetails().observe(getViewLifecycleOwner(),pictureResp -> {
            Log.d("vcbvcbcbcvcvds1234====",""+pictureResp.getStatus());
            switch(pictureResp.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if(pictureResp.getData() != null) {
                        setData(pictureResp.getData());
                    }
                    Log.d("vcbvcbcbcvcvds123445====",""+pictureResp.getData());
                    break;
                case ERROR:
                    Toast.makeText(activity,pictureResp.getError(),Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
    private void setData(NewTractorPictureResp data) {
        try {
            if(data.getTractor().getImage() != null) {
                urls.clear();
                String imgPatTh = AppConfig.BASE_ASSET_URL+"new-tractor/";
                Product product = new Product();
                product.setId(1);
                //product.setImageUrl(data.getOthers().getTractorImagePath()+data.getTractor().getImage().getTractor_img_name());

                product.setImageUrl(imgPatTh+data.getTractor().getImage().getTractor_img_name());
                urls.add(product);
                initView(urls);
                if(urls.size() != 1)
                    binding.indicator.setViewPager(binding.viewPager);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
