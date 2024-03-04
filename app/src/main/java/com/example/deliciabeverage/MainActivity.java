package com.example.deliciabeverage;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.deliciabeverage.databinding.ActivityMainBinding;
import com.example.deliciabeverage.model.BeverageCost;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    // String variable declaration for Beverage Type, Beverage Size, Beverage Flavor, Region, Store
    String beverageType = "Coffee";
    String beverageSize;
    String beverageFlavor;
    String region;
    String store;

    // Double variable declaration for Milk, Sugar and Size Cost.
    double milkCost = 0;
    double sugarCost = 0;
    double sizeCost = 0;

    // Flavor & Store Adapter initialization
    ArrayAdapter<CharSequence> adapterFlavor;
    ArrayAdapter<CharSequence> adapterStore;

    // Declare instance of date-picker
    DatePickerDialog datePicker;

    // BeverageCost Model declaration
    BeverageCost bCost;

    // Declare variable for bind reference of each element from xml file
    ActivityMainBinding binding;


    // Default method called when application run
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initiate view and binding
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bCost = new BeverageCost();

        // Make bill-view invisible
        binding.bill.setVisibility(View.INVISIBLE);

        // set each radio button unchecked except coffee radio
        binding.tea.setChecked(false);
        binding.coffee.setChecked(true);
        binding.rdSmall.setChecked(false);
        binding.rdMedium.setChecked(false);
        binding.rdLarge.setChecked(false);

        // set and bind array for flavor spinner dropdown
        adapterFlavor = ArrayAdapter.createFromResource(this, R.array.flavor_coffee, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.spnFlavor.setAdapter(adapterFlavor);

        // set and bind array for region auto complete view
        binding.acRegion.setThreshold(2);
        ArrayAdapter<CharSequence> adapterRegion = ArrayAdapter.createFromResource(this, R.array.region_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.acRegion.setAdapter(adapterRegion);

        // set input type to date input-field
        binding.edtDate.setInputType(InputType.TYPE_NULL);

        // function call
        setListeners();
    }


    // private function for set all widget listener
    private void setListeners(){
        binding.spnFlavor.setOnItemSelectedListener(this);
        binding.acRegion.setOnItemClickListener(this);
        binding.chkMilk.setOnClickListener(this);
        binding.chkSugar.setOnClickListener(this);
        binding.rgDrinkOption.setOnCheckedChangeListener(this);
        binding.edtDate.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.spnStore.setOnItemSelectedListener(this);
        binding.btnGoBack.setOnClickListener(this);
    }


    // View Listener default onClick method
    @Override
    public void onClick(View view) {
        if(view.getId() == binding.chkMilk.getId()){
            // Clicked widget ID is chkMilk
            if(binding.chkMilk.isChecked()){
                // Cost for selected milk
                milkCost = 1.25;
            }else{
                milkCost = 0;
            }
        }else if(view.getId() == R.id.chkSugar){
            // Clicked widget ID is chkSugar
            if(binding.chkSugar.isChecked()){
                // Cost for selected sugar
                sugarCost = 1.00;
            }else{
                sugarCost = 0;
            }
        }else if(view.getId() == R.id.edtDate){
            // Clicked widget ID is edtDate

            // initiate calender
            Calendar cal = Calendar.getInstance();

            // int variable for day, month and year
            int dayOfSales = cal.get(Calendar.DAY_OF_MONTH);
            int monthOfSales = cal.get(Calendar.MONTH);
            int yearOfSales = cal.get(Calendar.YEAR);

            // setting up date-picker dialog
            datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                // selected date set into date-field
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    binding.edtDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }
            }, yearOfSales, monthOfSales, dayOfSales);

            // disable future dates
            datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

            // show date-picker
            datePicker.show();

        }else if(view.getId() == R.id.btnSubmit){

            // set bill-view invisible when submit button clicked
            binding.bill.setVisibility(View.GONE);

            if (binding.edtName.getText().toString().isEmpty()) {
                // set error to the text input-field when customer name is empty
                binding.edtName.setError("Please enter name");
            } else if(binding.edtEmail.getText().toString().isEmpty()) {
                // set error to the text input-field when customer email is empty
                binding.edtEmail.setError("Please enter email");
            } else if(!isEmailValid(binding.edtEmail.getText().toString().trim())){
                // set error to the text input-field when customer email is not formatted
                binding.edtEmail.setError("Entered email is not valid");
            } else if(binding.edtPhone.getText().toString().isEmpty()) {
                // set error to the text input-field when customer phone number is empty
                binding.edtPhone.setError("Please enter phone number");
            } else if(!isPhoneValid(binding.edtPhone.getText().toString().trim())) {
                // set error to the text input-field when customer phone number is not formatted
                binding.edtPhone.setError("Entered phone number is not valid");
            } else if (!binding.chkMilk.isChecked() && !binding.chkSugar.isChecked()) {
                // give toast message when both check-box are empty
                Toast.makeText(getApplicationContext(), "Please Select at least one additional.", Toast.LENGTH_LONG).show();
            }  else if (!binding.rdSmall.isChecked() && !binding.rdMedium.isChecked() && !binding.rdLarge.isChecked()) {
                // give toast message when no one radio button is selected
                Toast.makeText(getApplicationContext(), "Please select beverage size", Toast.LENGTH_LONG).show();
            } else if (binding.acRegion.getText().toString().isEmpty()) {
                // set store adapter null if region is empty
                binding.spnStore.setAdapter(null);
                // set error to the text input-field when region is empty
                binding.acRegion.setError("Please select region.");
            } else if (!binding.acRegion.getText().toString().contains("Waterloo") && !binding.acRegion.getText().toString().contains("Milton") && !binding.acRegion.getText().toString().contains("London") && !binding.acRegion.getText().toString().contains("Mississauga") ) {
                // set store adapter null if region is invalid
                binding.spnStore.setAdapter(null);
                // set error to the text input-field when entered region not mach with Waterloo, Milton, London, Mississauga
                binding.acRegion.setError("Your entered region is not available.");
            } else if (binding.edtDate.getText().toString().isEmpty()) {
                // set error to the text input-field when date was not selected
                binding.edtDate.setError("Please select the sale date.");
            } else{
                // All fields are verified
                submit();
            }
        } else if (view.getId() == R.id.btnGoBack) {
            // set form visible and bill invisible
            binding.form.setVisibility(View.VISIBLE);
            binding.bill.setVisibility(View.GONE);
        }
    }


    // function to check given string is email or not
    public static boolean checkEmail(String strEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }
    // function to check given email is valid or not
    public static boolean isEmailValid(String strEmail) {
        return !TextUtils.isEmpty(strEmail) && checkEmail(strEmail);
    }


    // function to check given string is phone or not
    public static boolean checkPhone(String strPhone) {
        return Patterns.PHONE.matcher(strPhone).matches();
    }
    // function to check given phone is valid or not
    public static boolean isPhoneValid(String strPhone) {
        return !TextUtils.isEmpty(strPhone) && checkPhone(strPhone);
    }


    // private function for submit button (runs after all fields are verified)
    private void submit(){
        // clear all text input-fields error when all fields are verified
        binding.edtName.setError(null);
        binding.edtEmail.setError(null);
        binding.edtPhone.setError(null);
        binding.edtDate.setError(null);

        // build additional string
        String additional = "";
        if(binding.chkMilk.isChecked()){
            additional = binding.chkMilk.getText().toString();
        }
        if(binding.chkSugar.isChecked()){
            additional = additional.isEmpty() ? binding.chkSugar.getText().toString() : binding.chkMilk.getText().toString() + ", " + binding.chkSugar.getText().toString() ;
        }

        // set beverage size cost and string based on coffee or tea
        if(binding.rgBeverageSize.getCheckedRadioButtonId() == R.id.rdSmall){
            beverageSize = "Small";
            sizeCost =  beverageType.contains("Coffee") ?  1.75 : 1.50;
        }else if(binding.rgBeverageSize.getCheckedRadioButtonId() == R.id.rdMedium){
            beverageSize = "Medium";
            sizeCost =  beverageType.contains("Coffee") ? 2.75 :  2.50;
        }else if(binding.rgBeverageSize.getCheckedRadioButtonId() == R.id.rdLarge){
            beverageSize = "Large";
            sizeCost =  beverageType.contains("Coffee") ? 3.75 : 3.25;
        }


        // set all form data into a model
        bCost.setCustomerName(binding.edtName.getText().toString());
        bCost.setCustomerEmail(binding.edtEmail.getText().toString());
        bCost.setCustomerPhone(binding.edtPhone.getText().toString());
        bCost.setBeverageType(beverageType);
        bCost.setAdditional(additional);
        bCost.setBeverageSize(beverageSize);
        bCost.setMilkCost(milkCost);
        bCost.setSugarCost(sugarCost);
        bCost.setSizeCost(sizeCost);
        bCost.setBeverageFlavor(beverageFlavor);
        bCost.setRegion(region);
        bCost.setStore(store);
        bCost.setSalesDate(binding.edtDate.getText().toString());

        // get customer bill from model, according to provided data
        String customerBill = bCost.getCustomerBill();

        // set all fields data into text-view and display a bill
        binding.ansName.setText(bCost.getCustomerName());
        binding.ansEmail.setText(bCost.getCustomerEmail());
        binding.ansPhone.setText(bCost.getCustomerPhone());
        binding.ansBeverageType.setText(bCost.getBeverageType());
        binding.ansAdditional.setText(bCost.getAdditional());
        binding.ansBeverageSize.setText(bCost.getBeverageSize());
        binding.ansBeverageFlavor.setText(bCost.getBeverageFlavor());
        binding.ansRegion.setText(bCost.getRegion());
        binding.ansStore.setText(bCost.getStore());
        binding.ansStore.setText(bCost.getSalesDate());
        binding.ansSalesDate.setText(binding.edtDate.getText().toString());
        binding.ansTotal.setText( "$" + new DecimalFormat("##.##").format(bCost.getTotal()));
        binding.ansTax.setText( "$" + new DecimalFormat("##.##").format(bCost.getTax()));
        binding.ansFinal.setText( "$" + new DecimalFormat("##.##").format(bCost.getFinalCost()));


        // display snack-bar with all entered details
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), customerBill.toString(), Snackbar.LENGTH_INDEFINITE);

        // set snack-bar action
        snackBar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss snack-bar on "ok" click
                snackBar.dismiss();

                // set form invisible and bill visible
                binding.form.setVisibility(View.GONE);
                binding.bill.setVisibility(View.VISIBLE);
            }
        });

        // set colors of snack-bar
        snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.grey));
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.theme));
        snackBar.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackBar.setTextMaxLines(9);

        // display snack-bar
        snackBar.show();
    }


    // AdapterView Listener default onItemSelected method
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // set string of selected item of spinner widget to beverage flavor
        if(parent.getAdapter() == adapterFlavor){
            beverageFlavor = binding.spnFlavor.getItemAtPosition(position).toString();
        }else{
            if(region != null){
                // set store string if region is not empty or null
                store = binding.spnStore.getItemAtPosition(position).toString();
            }
        }
    }


    // AdapterView Listener default onNothingSelected method
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    // AdapterView Listener default onItemClick method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // set region string when region select from array list
        region = parent.getItemAtPosition(position).toString();

        // set the store adapter accordingly 4 different regions
        switch (region) {
            case "Waterloo":
                adapterStore = ArrayAdapter.createFromResource(this, R.array.waterloo_store, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                binding.spnStore.setAdapter(adapterStore);
                break;
            case "Milton":
                adapterStore = ArrayAdapter.createFromResource(this, R.array.milton_store, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                binding.spnStore.setAdapter(adapterStore);
                break;
            case "London":
                adapterStore = ArrayAdapter.createFromResource(this, R.array.london_store, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                binding.spnStore.setAdapter(adapterStore);
                break;
            case "Mississauga":
                adapterStore = ArrayAdapter.createFromResource(this, R.array.mississauga_store, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                binding.spnStore.setAdapter(adapterStore);
                break;
            default:
                break;
        }
    }


    // RadioGroup Listener default onCheckedChanged method
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(binding.rgDrinkOption.getCheckedRadioButtonId() == R.id.tea){
            // selected radio button is tea

            // set string type and flavor adapter based on tea
            beverageType = "Tea";
            adapterFlavor = ArrayAdapter.createFromResource(this, R.array.flavor_tea, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            binding.spnFlavor.setAdapter(adapterFlavor);
        }else if(binding.rgDrinkOption.getCheckedRadioButtonId() == R.id.coffee){
            // selected radio button is coffee

            // set string type and flavor adapter based on coffee
            beverageType = "Coffee";
            adapterFlavor = ArrayAdapter.createFromResource(this, R.array.flavor_coffee, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            binding.spnFlavor.setAdapter(adapterFlavor);
        }
    }

}