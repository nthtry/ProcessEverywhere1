package com.inter.aktiehq.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.aktiehq.app.TCP.TCPClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AktienlisteFragment extends Fragment {

    // Tag fÃ¼r das Logging des Fragment-Lifecycle definieren
    private final String LOG_TAG = AktienlisteFragment.class.getSimpleName();
    public TCPClient mTcpClient;
    // Der ArrayAdapter ist jetzt eine Membervariable der Klasse AktienlisteFragment
    ArrayAdapter<String> mBeaconlisteAdapter;

    public AktienlisteFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(LOG_TAG, "In Callback-Methode: onAttach()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "In Callback-Methode: onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "In Callback-Methode: onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "In Callback-Methode: onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "In Callback-Methode: onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "In Callback-Methode: onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(LOG_TAG, "In Callback-Methode: onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "In Callback-Methode: onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(LOG_TAG, "In Callback-Methode: onDetach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // MenÃ¼ bekannt geben, dadurch kann unser Fragment MenÃ¼-Events verarbeiten
        setHasOptionsMenu(true);
        Log.v(LOG_TAG, "In Callback-Methode: onCreate()");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_beaconlistefragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if (mTcpClient != null) {
            // if the client is connected, enable the connect button and disable the disconnect one
            menu.findItem(R.id.connect).setEnabled(false);
            menu.findItem(R.id.disconnect).setEnabled(true);
        } else {
            // if the client is disconnected, enable the disconnect button and disable the connect one
            menu.findItem(R.id.disconnect).setEnabled(false);
            menu.findItem(R.id.connect).setEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Wir prÃ¼fen, ob MenÃ¼-Element mit der ID "action_daten_aktualisieren"
        // ausgewÃ¤hlt wurde, holen die Finanzdaten und geben eine Meldung aus
        switch (item.getItemId()) {
            case R.id.action_beacon_aktualisieren:
                Toast.makeText(getActivity(), "settingsbeacon", Toast.LENGTH_SHORT).show();
                aktualisiereBeacons();
            case R.id.connect:
                // connect to the server
                new AktienlisteFragment.ConnectTask().execute("");
                return true;
            case R.id.disconnect:
                // disconnect
                mTcpClient.stopClient();
                mTcpClient = null;
                // notify the adapter that the data set has changed.
                //mAdapter.notifyDataSetChanged();
                return true;
            default:
                return true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "In Callback-Methode: onCreateView()");

        //Beaconpart Beginn
        String[] beaconlisteArray = {
                "Beacon1",
                "Beacon2",
                "Beacon3"
        };

        List<String> beaconListe = new ArrayList<>(Arrays.asList(beaconlisteArray));

        mBeaconlisteAdapter =
                new ArrayAdapter<>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_beaconliste, // ID der XML-Layout Datei
                        R.id.list_item_beaconliste_textview, // ID des TextViews
                        beaconListe); // Beispieldaten in einer ArrayList




        //Liste der Beacons in Rootview einfpgen
        View rootView = inflater.inflate(R.layout.fragment_beaconliste, container, false);

        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(0x000000);
        canvas.drawRect(50,50,50,50,paint );
        rootView.draw(canvas);

        //Chat am Footer
        View footerView = inflater.inflate(R.layout.chat, container, false);

        //ListView myListView = (ListView) findViewById(R.id.swipe_refresh_layout_beaconliste);
        LinearLayout chatFooter = (LinearLayout) footerView.findViewById(R.id.footer);

        //Liste erstellen
        ListView beaconlisteListView = (ListView) rootView.findViewById(R.id.listview_beaconliste);

        beaconlisteListView.setAdapter(mBeaconlisteAdapter);

        // Let's remove the footer view from it's current child...
        ((ViewGroup) chatFooter.getParent()).removeView(chatFooter);

        // ... and put it inside ListView.
        beaconlisteListView.addFooterView(chatFooter);

        beaconlisteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String beaconInfo = (String) adapterView.getItemAtPosition(position);

                // Intent erzeugen und Starten der AktiendetailActivity mit explizitem Intent
                Intent beacondetailIntent = new Intent(getActivity(), AktiendetailActivity.class);
                beacondetailIntent.putExtra(Intent.EXTRA_TEXT, beaconInfo);
                startActivity(beacondetailIntent);
            }
        });

        //TCP
        //rootView = inflater.inflate(R.layout.chat, container, false);
        final EditText editText = (EditText) rootView.findViewById(R.id.editText);
        Button send = (Button) rootView.findViewById(R.id.send_button);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString();

                //sends the message to the server
                if (mTcpClient != null) {
                    mTcpClient.sendMessage(message);
                }

                //refresh the list
                // mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        return rootView;

        //Beaconpart Ende
    }

    public void aktualisiereBeacons() {
        // Erzeugen einer Instanz von HoleDatenTask
        FindeBeaconsTask findeBeaconsTask = new FindeBeaconsTask();

        // Auslesen der ausgewÃ¤hlten Beacons aus den SharedPreferences
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefiBeaconlisteKey = getString(R.string.preference_iBeaconmodus_key);
        Boolean prefiBeaconlisteDefault = true;
        Boolean iBeaconSelected = sPrefs.getBoolean(prefiBeaconlisteKey, prefiBeaconlisteDefault);

        String prefEddystonelisteKey = getString(R.string.preference_eddystonemodus_key);
        Boolean prefEddystonelisteDefault = true;
        Boolean eddystoneSelected = sPrefs.getBoolean(prefEddystonelisteKey, prefEddystonelisteDefault);

        // Starten des asynchronen Tasks und Ãœbergabe der Aktienliste
        if (eddystoneSelected && iBeaconSelected) {
            findeBeaconsTask.execute("both");
        } else if (eddystoneSelected) {
            findeBeaconsTask.execute("eddystone");
        } else if (iBeaconSelected) {
            findeBeaconsTask.execute("iBeacon");
        }

        // Den Benutzer informieren, dass neue Aktiendaten im Hintergrund abgefragt werden
        Toast.makeText(getActivity(), "Beacons werden gesucht!", Toast.LENGTH_SHORT).show();
    }

   /* public void aktualisiereDaten() {
        // Erzeugen einer Instanz von HoleDatenTask
        HoleDatenTask holeDatenTask = new HoleDatenTask();

        // Auslesen der ausgewÃ¤hlten Aktienliste aus den SharedPreferences
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefAktienlisteKey = getString(R.string.preference_aktienliste_key);
        String prefAktienlisteDefault = getString(R.string.preference_aktienliste_default);
        String aktienliste = sPrefs.getString(prefAktienlisteKey, prefAktienlisteDefault);

        // Auslesen des Anzeige-Modus aus den SharedPreferences
        String prefIndizemodusKey = getString(R.string.preference_indizemodus_key);
        Boolean indizemodus = sPrefs.getBoolean(prefIndizemodusKey, false);

        // Starten des asynchronen Tasks und Ãœbergabe der Aktienliste
        if (indizemodus) {
            String indizeliste = "^GDAXI,^TECDAX,^MDAXI,^SDAXI,^GSPC,^N225,^HSI,XAGUSD=X,XAUUSD=X";
            holeDatenTask.execute(indizeliste);
        }
        else {
            holeDatenTask.execute(aktienliste);
        }

        // Den Benutzer informieren, dass neue Aktiendaten im Hintergrund abgefragt werden
        Toast.makeText(getActivity(), "Aktiendaten werden abgefragt!", Toast.LENGTH_SHORT).show();
    }*/

    public class ConnectTask extends AsyncTask<String, String, TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
/*
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();*/
        }
    }

    // Innere Klasse HoleDatenTask fÃ¼hrt den asynchronen Task auf eigenem Arbeitsthread aus
    public class FindeBeaconsTask extends AsyncTask<String, Integer, String[]> {

        private final String LOG_TAG = FindeBeaconsTask.class.getSimpleName();

        private String[] leseXmlAktiendatenAus(String xmlString) {


            /*Toast.makeText(getActivity(), "Beacons werden Task!", Toast.LENGTH_SHORT).show();
            Document doc;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                doc = db.parse(is);
            } catch (ParserConfigurationException e) {
                Log.e(LOG_TAG,"Error: " + e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e(LOG_TAG,"Error: " + e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG,"Error: " + e.getMessage());
                return null;
            }

            Element xmlAktiendaten = doc.getDocumentElement();
            NodeList aktienListe = xmlAktiendaten.getElementsByTagName("row");

            int anzahlAktien = aktienListe.getLength();
            int anzahlAktienParameter = aktienListe.item(0).getChildNodes().getLength();

            String[] ausgabeArray = new String[anzahlAktien];
            String[][] alleAktienDatenArray = new String[anzahlAktien][anzahlAktienParameter];

            Node aktienParameter;
            String aktienParameterWert;
            for( int i=0; i<anzahlAktien; i++ ) {
                NodeList aktienParameterListe = aktienListe.item(i).getChildNodes();

                for (int j=0; j<anzahlAktienParameter; j++) {
                    aktienParameter = aktienParameterListe.item(j);
                    aktienParameterWert = aktienParameter.getFirstChild().getNodeValue();
                    alleAktienDatenArray[i][j] = aktienParameterWert;
                }

                ausgabeArray[i]  = alleAktienDatenArray[i][0];                // symbol
                ausgabeArray[i] += ": " + alleAktienDatenArray[i][4];         // price
                ausgabeArray[i] += " " + alleAktienDatenArray[i][2];          // currency
                ausgabeArray[i] += " (" + alleAktienDatenArray[i][8] + ")";   // percent
                ausgabeArray[i] += " - [" + alleAktienDatenArray[i][1] + "]"; // name

                Log.v(LOG_TAG,"XML Output:" + ausgabeArray[i]);
            }*/

            String[] ausgabeArray = new String[3];
            ausgabeArray[0] = "Beacon1";
            ausgabeArray[1] = "Beacon2";
            ausgabeArray[2] = "Beacon3";

            return ausgabeArray;
        }

        @Override
        protected String[] doInBackground(String... strings) {

            if (strings.length == 0) { // Keine Eingangsparameter erhalten, daher Abbruch
                return null;
            }


            String aktiendatenXmlString = "Beacon1 \n Beacon2 \n Beacon3 \n";
            return leseXmlAktiendatenAus(aktiendatenXmlString);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            // Auf dem Bildschirm geben wir eine Statusmeldung aus, immer wenn
            // publishProgress(int...) in doInBackground(String...) aufgerufen wird
            // Toast.makeText(getActivity(), values[0] + " von " + values[1] + " geladen",
            //        Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String[] strings) {

            // Wir lÃ¶schen den Inhalt des ArrayAdapters und fÃ¼gen den neuen Inhalt ein
            // Der neue Inhalt ist der RÃ¼ckgabewert von doInBackground(String...) also
            // der StringArray gefÃ¼llt mit Beispieldaten
            if (strings != null) {
                mBeaconlisteAdapter.clear();
                for (String beaconString : strings) {
                    mBeaconlisteAdapter.add(beaconString);
                }
            }

            // Hintergrundberechnungen sind jetzt beendet, darÃ¼ber informieren wir den Benutzer
            Toast.makeText(getActivity(), "Suche nach Beacons beendet!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}