package com.khetigaadi.tractor.newTractor;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import com.khetigaadi.R;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.Price;
import com.khetigaadi.commons.listFilter.CityFilterAdapter;
import com.khetigaadi.commons.listFilter.HPFilterAdapter;
import com.khetigaadi.commons.listFilter.ListFilterBrandItemAdapter1;
import com.khetigaadi.commons.listFilter.ListFilterDriveAdapter;
import com.khetigaadi.commons.listFilter.TypeFilterAdapter;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.databinding.NewFilterListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.room.repository.CityRepository;
import com.khetigaadi.room.repository.HpRepository;
import com.khetigaadi.room.repository.TypeRepository;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.util.NumberFormatting;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NewTractorListFilter extends Fragment {
    private static final String TAG = NewTractorListFilter.class.getSimpleName();
    private ListFilterBrandItemAdapter1 mBrandNameAdapter;
    private HPFilterAdapter mHPNamesAdapter;
    private Filter filter;
    private FragmentActivity activity;
    @Inject
    BrandRepository brandRepository;
    @Inject
    HpRepository hpRepository;
    @Inject
    TypeRepository typeRepository;
    @Inject
    CityRepository cityRepository;
    private NewFilterListBinding binding;
    private ListFilterDriveAdapter driveAdapter;
    @CallFrom
    private int callFrom;
    private TypeFilterAdapter typesAdapter;
    private CityFilterAdapter cityAdapter;
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        activity = getActivity();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        readData(activity.getIntent().getExtras());
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        binding = NewFilterListBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById();
        binding.btnClearAll.setOnClickListener(v -> {
            binding.etBrand.setText("");
            binding.etHP.setText("");
            mBrandNameAdapter.clearSelection();
            mHPNamesAdapter.clearSelection();
            driveAdapter.clearSelection();
            typesAdapter.clearSelection();
            binding.etCity.setText("");
            cityAdapter.clearSelection();
            binding.rangeBarPrice.setValues(0f, 37f);
            filter.clear();
        });
        binding.btnApplyFilter.setOnClickListener(rippleView -> {
            if(filter != null) {
                if(binding.cvSearchByBrand.getVisibility() == View.VISIBLE)
                    filter.setBrands(mBrandNameAdapter.getSelectedItems());
                if(binding.cvSearchByHP.getVisibility() == View.VISIBLE)
                    filter.setHps(mHPNamesAdapter.getSelectedItems());
                if(binding.cvSearchByDrive.getVisibility() == View.VISIBLE)
                    filter.setDrives(driveAdapter.getSelectedItems());
                if(binding.cvSearchByPrice.getVisibility() == View.VISIBLE)
                    filter.setPrice(new Price(NumberFormatting.getPriceFormIndex(Math.round(binding.rangeBarPrice.getValues().get(0))),NumberFormatting.getPriceFormIndex(Math.round(binding.rangeBarPrice.getValues().get(1)))));
                if(binding.cvSearchByType.getVisibility() == View.VISIBLE)
                    filter.setTypes(typesAdapter.getSelectedItems());
                if(binding.cvSearchByCity.getVisibility() == View.VISIBLE)
                    filter.setCity(cityAdapter.getSelectedItems());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("filter",filter);
                activity.setResult(Activity.RESULT_OK,returnIntent);
                activity.finish();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if(callFrom == CallFrom.NEW_TRACTOR || callFrom == CallFrom.DEALER) {
            brandRepository.getNewTractorBrands().observe(getViewLifecycleOwner(),new Observer<List<Brand>>() {
                @Override
                public void onChanged(@Nullable List<Brand> brands) {
                    try {
                        Log.d("vxcvcxvcxvxvxcv===",""+brands.size());
                        if(brands.size()>0) {
                            List<Brand> brandArray1 = new ArrayList<>();
                            for(Iterator<Brand> it = brands.iterator(); it.hasNext(); ) {
                                Brand item = it.next();
                                item.setSelected(filter.getBrands() != null && filter.getBrands().contains(item));
                                brandArray1.add(item);
                            }
                            mBrandNameAdapter.updateItems(brandArray1);
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }
        else if(callFrom == CallFrom.OLD_TRACTOR || callFrom == CallFrom.RENT_TRACTOR) {
            brandRepository.getOldTractorBrands().observe(getViewLifecycleOwner(),new Observer<List<com.khetigaadi.implement.newImplement.util.Brand>>() {
                @Override
                public void onChanged(@Nullable List<com.khetigaadi.implement.newImplement.util.Brand> brands) {
                    try {
                        assert brands != null;
                        if(!brands.isEmpty()) {
                            List<Brand> brandArray1 = new ArrayList<>();
                            for (Iterator<Brand> it = brands.iterator(); it.hasNext(); ) {
                                Brand item = it.next();
                                item.setSelected(filter.getBrands() != null && filter.getBrands().contains(item));
                                brandArray1.add(item);
                            }
                            mBrandNameAdapter.updateItems(brandArray1);
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if(callFrom == CallFrom.IMPLEMENT) {
            brandRepository.getImplementBrandsAsc().observe(getViewLifecycleOwner(),new Observer<List<com.khetigaadi.implement.newImplement.util.Brand>>() {
                @Override
                public void onChanged(@Nullable List<com.khetigaadi.implement.newImplement.util.Brand> brands) {
                    try {
                    if(brands.size()>0) {
                        List<Brand> brandArray1 = new ArrayList<>();
                        for (Iterator<Brand> it = brands.iterator(); it.hasNext(); ) {
                            Brand item = it.next();
                            item.setSelected(filter.getBrands() != null && filter.getBrands().contains(item));
                            brandArray1.add(item);
                        }
                        mBrandNameAdapter.updateItems(brandArray1);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                }
            });
        }
        else if(callFrom == CallFrom.HARVESTER) {
            brandRepository.getHarvesterBrands().observe(getViewLifecycleOwner(), brands -> {
                try {
                    if (brands.size()>0) {
                        List<Brand> brandArray1 = new ArrayList<>();
                        for (Iterator<Brand> it = brands.iterator(); it.hasNext(); ) {
                            Brand item = it.next();
                            item.setSelected(filter.getBrands() != null && filter.getBrands().contains(item));
                            brandArray1.add(item);
                        }
                        mBrandNameAdapter.updateItems(brandArray1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        if(callFrom != CallFrom.HARVESTER) {
            hpRepository.getHPs().observe(getViewLifecycleOwner(), listResource -> {
                if (listResource.size()>0) {
                    List<Hp> hps = listResource;
                    List<Hp> hpList = new ArrayList<>();
                    try {
                        if (hps != null) {
                            for (int i = 0; i < hps.size(); i++) {
                                Hp obj = hps.get(i);
                                obj.setSelected(filter.getHps() != null && filter.getHps().contains(hps.get(i)));
                                hpList.add(obj);
                            }
                            mHPNamesAdapter.updateItems(hpList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if(callFrom == CallFrom.IMPLEMENT) {
            typeRepository.getTypes().observe(getViewLifecycleOwner(),listResource -> {
                if (listResource.size()>0) {
                    List<Type> hps = listResource;
                    List<Type> hpList = new ArrayList<>();
                    try {
                        if (hps != null) {
                            for (int i = 0; i < hps.size(); i++) {
                                Type obj = hps.get(i);
                                obj.setSelected(filter.getTypes() != null && filter.getTypes().contains(hps.get(i)));
                                hpList.add(obj);
                            }
                            typesAdapter.updateItems(hpList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if(callFrom == CallFrom.HARVESTER) {
            typeRepository.getHarvesterTypes().observe(getViewLifecycleOwner(),listResource -> {
                if (listResource.size()>0) {
                    List<Type> hps = listResource;
                    List<Type> hpList = new ArrayList<>();
                    try {
                        if (hps != null) {
                            for (int i = 0; i < hps.size(); i++) {
                                Type obj = hps.get(i);
                                obj.setSelected(filter.getTypes() != null && filter.getTypes().contains(hps.get(i)));
                                hpList.add(obj);
                            }
                            typesAdapter.updateItems(hpList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if(callFrom == CallFrom.OLD_TRACTOR || callFrom == CallFrom.RENT_TRACTOR || callFrom == CallFrom.DEALER) {
            cityRepository.getCities().observe(getViewLifecycleOwner(), listResource -> {
                if (listResource.size()>0) {
                    List<City> hps = listResource;
                    List<City> hpList = new ArrayList<>();
                    try {
                        if (hps != null) {
                            for (int i = 0; i < hps.size(); i++) {
                                City obj = hps.get(i);
                                obj.setSelected(filter.getCity() != null && filter.getCity().contains(hps.get(i)));
                                hpList.add(obj);
                            }
                            cityAdapter.updateItems(hpList);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    private void readData(Bundle bundle) {
        if(bundle != null) {
            filter =   bundle.getParcelable("filter");
            Log.d("fvdsfsfdsfdsfs===",""+filter);
            callFrom = bundle.getInt("callFrom",CallFrom.NEW_TRACTOR);
        }
    }
    private void findViewById() {
        mBrandNameAdapter = new ListFilterBrandItemAdapter1();
        driveAdapter = new ListFilterDriveAdapter(getDriveNames());
        typesAdapter = new TypeFilterAdapter();
        mHPNamesAdapter = new HPFilterAdapter();
        cityAdapter = new CityFilterAdapter();
        binding.rvBrands.setAdapter(mBrandNameAdapter);
        binding.etBrand.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s,int start,int before,int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                mBrandNameAdapter.getFilter().filter(s.toString());
            }
        });
        if(callFrom == CallFrom.OLD_TRACTOR)
        {
            if(filter.getPrice() != null) {
                binding.rangeBarPrice.setValues((float) NumberFormatting.getIndexFromPrice(filter.getPrice().getPriceFrom()).get(), (float) NumberFormatting.getIndexFromPrice(filter.getPrice().getPriceTo()).get());
                selectPriceButton(Math.round(binding.rangeBarPrice.getValues().get(0)), Math.round(binding.rangeBarPrice.getValues().get(1)));
            }
            else {
                binding.rangeBarPrice.setValues(0f, 37f);
            }
            binding.tvMinPrice.setText(String.format("%s%s",getString(R.string.rs),NumberFormatting.getShortPriceFromIndex(Math.round(binding.rangeBarPrice.getValues().get(0)))));
            binding.tvMaxPrice.setText(String.format("%s%s",getString(R.string.rs),NumberFormatting.getShortPriceFromIndex(Math.round(binding.rangeBarPrice.getValues().get(1)))));
            binding.rangeBarPrice.addOnChangeListener((slider, value, fromUser) -> {
                binding.tvMinPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.getShortPriceFromIndex(Math.round(slider.getValues().get(0)))));
                binding.tvMaxPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.getShortPriceFromIndex(Math.round(slider.getValues().get(1)))));
                selectPriceButton(Math.round(slider.getValues().get(0)), Math.round(slider.getValues().get(1)));
            });
            binding.rangeBarPrice.setLabelFormatter(value -> String.format("%s%s", getString(R.string.rs),NumberFormatting.getShortPriceFromIndex(Math.round(value))));
            binding.radio10To2.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    binding.rangeBarPrice.setValues(0f, 11f);
            });
            binding.radio2To3.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    binding.rangeBarPrice.setValues(11f, 13f);
            });
            binding.radio3To5.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    binding.rangeBarPrice.setValues(13f, 17f);
            });
            binding.radioAbove5.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    binding.rangeBarPrice.setValues(17f, 37f);
            });
        }
        else
        {
            binding.cvSearchByPrice.setVisibility(View.GONE);
        }
        if(callFrom == CallFrom.NEW_TRACTOR || callFrom == CallFrom.OLD_TRACTOR)
        {
            binding.rvDrive.setAdapter(driveAdapter);
        }
        else
        {
            binding.cvSearchByDrive.setVisibility(View.GONE);
        }
        if(callFrom == CallFrom.IMPLEMENT || callFrom == CallFrom.HARVESTER)
        {
            binding.rvType.setAdapter(typesAdapter);
            binding.etType.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    typesAdapter.getFilter().filter(s.toString());
                }
            });
        }
        else
        {
            binding.cvSearchByType.setVisibility(View.GONE);
        }
        if(callFrom == CallFrom.DEALER || callFrom == CallFrom.HARVESTER)
        {
            binding.cvSearchByHP.setVisibility(View.GONE);
        }
        else
        {
            binding.rvHPs.setAdapter(mHPNamesAdapter);
            binding.etHP.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s,int start,int before,int count) {
                }
                @Override
                public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    mHPNamesAdapter.getFilter().filter(s.toString());
                }
            });
        }
        if(callFrom == CallFrom.OLD_TRACTOR || callFrom == CallFrom.RENT_TRACTOR || callFrom == CallFrom.DEALER)
        {
            binding.rvCity.setAdapter(cityAdapter);
            binding.etCity.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s,int start,int before,int count) {
                }
                @Override
                public void beforeTextChanged(CharSequence s,int start,int count,int after) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    cityAdapter.getFilter().filter(s.toString());
                }
            });
        }
        else
        {
            binding.cvSearchByCity.setVisibility(View.GONE);
        }
    }
    private void selectPriceButton(int leftPinIndex,int rightPinIndex) {
        if (leftPinIndex == 0 && rightPinIndex == 11) {
            binding.radio10To2.setChecked(true);
        } else if (leftPinIndex == 11 && rightPinIndex == 13) {
            binding.radio2To3.setChecked(true);
        } else if (leftPinIndex == 13 && rightPinIndex == 17) {
            binding.radio3To5.setChecked(true);
        } else if (leftPinIndex == 17 && rightPinIndex == 37)
            binding.radioAbove5.setChecked(true);
        else {
            binding.radio10To2.setChecked(false);
            binding.radio2To3.setChecked(false);
            binding.radio3To5.setChecked(false);
            binding.radioAbove5.setChecked(false);
        }
    }
    private List<Drive> getDriveNames() {
        ArrayList<Drive> driveList = new ArrayList<>();
        Drive wd2 = new Drive(1,getString(R.string.wd2),false);
        Drive wd4 = new Drive(2,getString(R.string.wd4),false);
        if(filter.getDrives() != null && filter.getDrives().contains(wd2)) {
            driveList.add(new Drive(1,getString(R.string.wd2),true));
        }
        else {
            driveList.add(wd2);
        }
        if(filter.getDrives() != null && filter.getDrives().contains(wd4)) {
            driveList.add(new Drive(2,getString(R.string.wd4),true));
        }
        else {
            driveList.add(wd4);
        }
        return driveList;
    }
}
