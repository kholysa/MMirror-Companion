package residence.kholy.helloworldtest;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String[] cities = {"Alexandria","Kuwait", "Montreal", "London" };
        Arrays.sort(cities);

        String[] A_NYSEStocks = {"AAPL","AMZN","ADBE","CSCO"};
        String[] E_NYSEStocks = {"EBAY","FB","GOOG","INTC"};
        String[] J_NYSEStocks = {"NFLX","NVDA","PYPL","QCOM"};
        String[] T_NYSEStocks = {"TSLA","TXN","FOX","YHOO"};
        Arrays.sort(A_NYSEStocks);
        Arrays.sort(E_NYSEStocks);
        Arrays.sort(J_NYSEStocks);
        Arrays.sort(T_NYSEStocks);

        String[] A_IntlStocks = {"FTSE 100","FTSE 250","NIKKEI"};
        String[] E_IntlStocks = {"Brent Crude Oil","WTI","S&P 500"};

        Arrays.sort(A_IntlStocks);
        Arrays.sort(E_IntlStocks);

        TextView Weather = (TextView) findViewById(R.id.textViewWeather);
        Weather.setTextColor(Color.argb(255,255,221,171));

        TextView chooseWeather = (TextView) findViewById(R.id.txtViewChooseCity);
        chooseWeather.setTextColor(Color.argb(255,255,221,171));

        TextView Stocks = (TextView) findViewById(R.id.textViewStocks);
        Stocks.setTextColor(Color.argb(255,255,221,171));


        TextView NASDAQ = (TextView) findViewById(R.id.textViewNASDAQ);
        NASDAQ.setTextColor(Color.argb(255,255,221,171));

        TextView intlStks = (TextView) findViewById(R.id.textViewIntlStocks);
        intlStks.setTextColor(Color.argb(255,255,221,171));

        Spinner citiesList = (Spinner) findViewById(R.id.spinnerCities);

        Spinner spinnerA = (Spinner) findViewById(R.id.spinnerA);
        Spinner spinnerE = (Spinner) findViewById(R.id.spinnerE);
        Spinner spinnerJ = (Spinner) findViewById(R.id.spinnerJ);
        Spinner spinnerT = (Spinner) findViewById(R.id.spinnerT);

        Spinner spinnerIntlA = (Spinner) findViewById(R.id.spinnerIntlA);
        Spinner spinnerIntlE = (Spinner) findViewById(R.id.spinnerIntlE);

        ArrayAdapter<String> test = new ArrayAdapter<String>(this, R.layout.spinner_item,cities);
        citiesList.setAdapter(test);


        ArrayAdapter<String> A = new ArrayAdapter<String>(this, R.layout.spinner_item,A_NYSEStocks);
        ArrayAdapter<String> E = new ArrayAdapter<String>(this, R.layout.spinner_item,E_NYSEStocks);
        ArrayAdapter<String> J = new ArrayAdapter<String>(this, R.layout.spinner_item,J_NYSEStocks);
        ArrayAdapter<String> T = new ArrayAdapter<String>(this, R.layout.spinner_item,T_NYSEStocks);


        ArrayAdapter<String> AIntl = new ArrayAdapter<String>(this, R.layout.spinner_item,A_IntlStocks);
        ArrayAdapter<String> EIntl = new ArrayAdapter<String>(this, R.layout.spinner_item,E_IntlStocks);

        spinnerA.setAdapter(A);
        spinnerE.setAdapter(E);
        spinnerJ.setAdapter(J);
        spinnerT.setAdapter(T);


        spinnerIntlA.setAdapter(AIntl);
        spinnerIntlE.setAdapter(EIntl);

        Button crtJSON = (Button) findViewById(R.id.buttonCrtSndJSON);



        crtJSON.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View v) {
                Settings cjo = new Settings();

                Spinner selected = (Spinner) findViewById(R.id.spinnerCities);

                String selectedCity = selected.getSelectedItem().toString();
                cjo.setCity(selectedCity);

                Spinner spinnerA = (Spinner) findViewById(R.id.spinnerA);
                Spinner spinnerE = (Spinner) findViewById(R.id.spinnerE);
                Spinner spinnerJ = (Spinner) findViewById(R.id.spinnerJ);
                Spinner spinnerT = (Spinner) findViewById(R.id.spinnerT);

                Spinner spinnerIntlA = (Spinner) findViewById(R.id.spinnerIntlA);
                Spinner spinnerIntlE = (Spinner) findViewById(R.id.spinnerIntlE);

                cjo.addStock(spinnerA.getSelectedItem().toString());
                cjo.addStock(spinnerE.getSelectedItem().toString());
                cjo.addStock(spinnerJ.getSelectedItem().toString());
                cjo.addStock(spinnerT.getSelectedItem().toString());

                String intlA = spinnerIntlA.getSelectedItem().toString();
                if (intlA=="FTSE 100"){
                    intlA = "^FTSE";
                }else if (intlA=="FTSE 250"){
                    intlA = "^FTMC";
                }else if (intlA=="NIKKEI"){
                    intlA = "^N225";
                }

                String intlE = spinnerIntlE.getSelectedItem().toString();
                if (intlE=="Brent Crude Oil"){
                    intlE = "BZZ15.NYM";
                }else if (intlE=="S&P 500"){
                    intlE = "^GSPC";
                }else if (intlE=="WTI"){
                    intlE = "WTI";
                }

                cjo.addStock(intlA);
                cjo.addStock(intlE);



                Gson gs = new Gson();
                String gsString = gs.toJson(cjo);

                try {
                    FileOutputStream fOut = openFileOutput("settings.json",MODE_WORLD_READABLE);
                    fOut.write(gsString.getBytes());
                    fOut.close();
                    Toast.makeText(getBaseContext(),"Ready to send", Toast.LENGTH_SHORT).show();
                    startBluetoothTransfer(gsString);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getBaseContext(),"Can't save here",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(),"Can't write file",Toast.LENGTH_SHORT).show();
                }

            }

                private void startBluetoothTransfer(String input) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        // Device does not support Bluetooth
                        Toast.makeText(getBaseContext(),"Device doesn't support bluetooth",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent discoverableIntent = new
                                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                        startActivity(discoverableIntent);

                    }
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, input);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
