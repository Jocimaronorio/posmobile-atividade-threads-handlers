package br.com.posmobile.previsaodotempo;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrincipalActivity extends AppCompatActivity {


    ListView listaPrevisoes;
    List<Previsao> previsoes = new ArrayList<Previsao>();

    TextView tvTemperaturaHoje;
    TextView tvPeriodoHoje;
    ImageView ivIconeHoje;

    Retrofit retrofit;
    PrevisoesAPI previsoesAPI;
    
    Thread tarefaAtualizacao;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        listaPrevisoes = (ListView) findViewById(R.id.previsoesListView);
        tvTemperaturaHoje = (TextView) findViewById(R.id.textViewTempHoje);
        tvPeriodoHoje = (TextView) findViewById(R.id.textViewPeriodoHoje);
        ivIconeHoje = (ImageView) findViewById(R.id.imageViewIconeHoje);

        listaPrevisoes.setAdapter(new ListaPrevisaoAdapter(this, previsoes));
        configurarAPI();

        //todo Inicialize o atributo handler utilizando o Looper da thread principal (main)
        atualizacaoAutomaticaDasPrevisoes();

    }

    private void configurarAPI() {
        GsonBuilder gsonBldr = new GsonBuilder();
        gsonBldr.registerTypeAdapter(Previsao.class, new PrevisaoDeserializer());

        retrofit = new Retrofit.Builder()
                .baseUrl(Utils.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gsonBldr.create()))
                .build();

        previsoesAPI = retrofit.create(PrevisoesAPI.class);
        Call<Previsoes> callbackPrevisoes = previsoesAPI.getPrevisoes("vitoria,brazil", Utils.API_KEY);
        callbackPrevisoes.enqueue(new Callback<Previsoes>() {
            @Override
            public void onResponse(Call<Previsoes> call, retrofit2.Response<Previsoes> response) {
                Previsoes previsoes = response.body();
                atualizaPrevisoes(previsoes.previsaoList);
            }

            @Override
            public void onFailure(Call<Previsoes> call, Throwable t) {

            }
        });
    }

    private void atualizaPrevisoes(List<Previsao> previsoes) {
        PrincipalActivity.this.previsoes.clear();
        Previsao previsaoDestaque = previsoes.remove(0);
        tvTemperaturaHoje.setText(previsaoDestaque.getTemperatura());
        tvPeriodoHoje.setText(previsaoDestaque.getPeriodo());

        Glide.with(PrincipalActivity.this).
                load(String.format(Utils.URL_ICONE, previsaoDestaque.getIcone())).
                into(ivIconeHoje);

        PrincipalActivity.this.previsoes.addAll(previsoes);
        ((ArrayAdapter) PrincipalActivity.this.listaPrevisoes.getAdapter()).notifyDataSetChanged();
    }


    private void atualizacaoAutomaticaDasPrevisoes() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                        while (true) {
                            //todo Coloque a thread para dormir alguns segundos até a próxima busca de previsões....
                            Call<Previsoes> callbackPrevisoes = previsoesAPI.getPrevisoes("vitoria,brazil", Utils.API_KEY);
                            callbackPrevisoes.enqueue(new Callback<Previsoes>() {
                                @Override
                                public void onResponse(Call<Previsoes> call, retrofit2.Response<Previsoes> response) {
                                    final Previsoes previsoesAutomaticas = response.body();
                                    //todo Utilize o método post do handler para atualizar a interface do usuário
                                    //todo Aproveite e avise com um Toast que as previsões foram atualizadas...

                                }
                                @Override
                                public void onFailure(Call<Previsoes> call, Throwable t) {

                                }
                            });
                        }

            }
        };
        //todo Crie uma nova Thread com o runnable e atribua para o tarefaAtualizacao
    }

    //todo Sobreescreva o onStart e inicialize sua Thread para checagem automática (lembre-se de verificar se a thread foi criada)

    //todo Sobreescreva o onPause e interrompa sua Thread (lembre-se de verificar se a thread foi criada)

}
