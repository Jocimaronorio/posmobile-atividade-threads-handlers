package br.com.posmobile.previsaodotempo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alexandre on 07/08/16.
 */

public class Previsoes {

    @SerializedName("list")
    List<Previsao> previsaoList;

}
