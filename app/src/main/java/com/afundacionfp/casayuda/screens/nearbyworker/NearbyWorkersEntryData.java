package com.afundacionfp.casayuda.screens.nearbyworker;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * Worker data entry container.
 * Saves a data entry for a RecyclerView cell.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class NearbyWorkersEntryData {
    private int id;
    private String name;
    private String price;
    private String services;

    NearbyWorkersEntryData(int id, String name, String price, List<String> services) {
        this.id = id;
        this.name = name;
        this.price = price;
        StringBuilder st = new StringBuilder();
        services.forEach(service -> {
                    if(!st.toString().equals(""))
                        st.append(", ");
                    st.append(service);
                });
        this.services = st.toString();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getServices() {
        return services;
    }
}
