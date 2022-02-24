package com.afundacionfp.casayuda.client;

import android.content.Context;
import android.content.SharedPreferences;
import com.afundacionfp.casayuda.client.dtos.JobRequestObjectDto;
import com.afundacionfp.casayuda.client.dtos.LoginUserBodyResponseDto;
import com.afundacionfp.casayuda.client.dtos.ObjectSelfEmployedDto;
import com.afundacionfp.casayuda.client.handlers.GetNearbyWorkersHandler;
import com.afundacionfp.casayuda.client.handlers.GetWorkerRequestById;
import com.afundacionfp.casayuda.client.handlers.LoginResponseHandler;
import com.afundacionfp.casayuda.client.handlers.PostWorkerCommentHandler;
import com.afundacionfp.casayuda.client.handlers.UserResponseHandler;
import com.afundacionfp.casayuda.client.dtos.workerdetailbodyresponsedto.WorkerDetailBodyResponseDto;
import com.afundacionfp.casayuda.client.handlers.DefaultErrorHandler;
import com.afundacionfp.casayuda.client.handlers.PostAcceptWorkerRequestHandler;
import com.afundacionfp.casayuda.client.handlers.GetWorkerDetailHandler;
import com.afundacionfp.casayuda.client.handlers.PostHireWorkerHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient {
    private RequestQueue queue;
    private Context context;

    // La resolución del nombre de dominio en el DNS de la red local puede dar
    // problemas en ciertos emuladores.
    // La siguiente dirección usa la IP del servidor local en lugar de su nombre de dominio.
    private final String REST_API_BASE_URL = "http://192.168.0.115:8002/rest";

    // Para más info sobre Android Emulator Networking:
    // - https://developer.android.com/studio/run/emulator-networking#networkaddresses


    private static RestClient instance = null;

    private RestClient(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public static RestClient getInstance(Context context) {
        if (instance == null) {
            instance = new RestClient(context);
        }
        return instance;
    }

    /**
     * Sends an appointment to the server.
     * @author mjaimesl
     * @param id Worker ID.
     *
     * @param date Comment date
     * @param message Comment message
     * @param handler Response handler.
     */
    public void postHireWorker(int id, String date, String message,
                               PostHireWorkerHandler handler) {
        JSONObject data = new JSONObject();
        try {
            data.put("requested_datetime", date);
            data.put("message", message);
        }catch(JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/version/1/workers/" + id + "/requests",
                data,
                response -> handler.requestDidComplete(),
                new DefaultErrorHandler(handler)
        ) {
            // Aquí sobreescribimos el método getHeaders de JsonObjectRequest para customizar las cabeceras
            // Si no es necesario añadir un token u otro parámetro en las cabeceras, podemos ahorrárnoslo.
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> cabeceras = new HashMap<>(super.getHeaders());
                SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String savedtoken= sharedPref.getString("token",null);
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    cabeceras.put("token", savedtoken);
                }
                return cabeceras;
            }
        };
        queue.add(request);
    }

    /**
     * Requests worker data by ID to the server.
     * @author mjaimesl
     * @param id Worker ID.
     * @param hiredByUserId User ID which hires the worker.
     *
     * @param handler       Request handler.
     */
    public void getWorkerDetail(int id, int hiredByUserId,
                                GetWorkerDetailHandler handler) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/version/1/workers/" + id + "?hired_by_user_id=" + hiredByUserId,
                null,
                response -> {
                    try {
                        handler.requestDidComplete(new WorkerDetailBodyResponseDto(response));
                    } catch (JSONException e) {
                        handler.requestDidFail(-1);
                        e.printStackTrace();
                    }
                },
                new DefaultErrorHandler(handler)

        ) {
            // Aquí sobreescribimos el método getHeaders de JsonObjectRequest para customizar las cabeceras
            // Si no es necesario añadir un token u otro parámetro en las cabeceras, podemos ahorrárnoslo.
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> cabeceras = new HashMap<>(super.getHeaders());
                SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String savedtoken= sharedPref.getString("token",null);
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    cabeceras.put("token", savedtoken);
                }
                return cabeceras;
            }
        };
        queue.add(request);
    }

    public void postUserLogin(String email, String password, LoginResponseHandler handler) {
        JSONObject cuerpoPeticion = new JSONObject();
        try {
            cuerpoPeticion.put("email", email);
            cuerpoPeticion.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest petition = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/version/1/sessions",
                cuerpoPeticion,
                response -> {

                    try {
                        LoginUserBodyResponseDto logindto = new LoginUserBodyResponseDto(response);

                        //Guardo esos valores en un objeto SharedPreferences usando su Editor
                        SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("token", logindto.getToken());
                        editor.putBoolean("is_worker", logindto.isIs_worker());
                        editor.putInt("user_id",logindto.getUser_id());
                        editor.commit();


                        handler.sessionsRequestDidComplete(logindto);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new DefaultErrorHandler(handler)
        );
        queue.add(petition);
    }

    public void postUserRegister(String name, String surname, String email, String password,
                             String[] jobs, int price_per_hour, double latitude, double longitude,
                             UserResponseHandler handler) {
        JSONObject cuerpoPeticion = new JSONObject();
        try {
            cuerpoPeticion.put("name", name);
            cuerpoPeticion.put("surname", surname);
            cuerpoPeticion.put("email", email);
            cuerpoPeticion.put("password", password);
            if (jobs != null) {
                JSONObject workerObject = new JSONObject();
                JSONArray jobsArray = new JSONArray();
                for (String job: jobs) {
                    jobsArray.put(job);
                }
                workerObject.put("jobs", jobsArray);
                workerObject.put("price_per_hour", price_per_hour);
                workerObject.put("latitude", latitude);
                workerObject.put("longitude", longitude);
                cuerpoPeticion.put("worker", workerObject);
            }
            System.out.println(cuerpoPeticion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest petition = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/version/1/users",
                cuerpoPeticion,
                response -> handler.sessionsRequestDidComplete(), new DefaultErrorHandler(handler)
        );
        queue.add(petition);
    }

    public void getNearbyWorkers(double latitude, double longitude, double radius,
                                  GetNearbyWorkersHandler handler) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/version/1/workers?latitude=" + latitude + "&longitude=" +
                        longitude + "&radius=" + radius,
                null,
                response -> {
                    // Here we decode the response.
                    // Dto objects make that easier for us.
                    List<ObjectSelfEmployedDto> decodedWorkersList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject currentIterationElement = response.getJSONObject(i);
                            decodedWorkersList.add(new ObjectSelfEmployedDto(currentIterationElement));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.requestDidFail(-1);
                        }
                    }
                    // Never forget to call the handler method!
                    handler.WorkersRequestDidComplete(decodedWorkersList);
                },
                new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String savedtoken= sharedPref.getString("token",null);
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("token", savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void postWorkerComment(int id, int rating, String comment,  PostWorkerCommentHandler handler) {
        JSONObject cuerpoPeticion = new JSONObject();
        try {
            cuerpoPeticion.put("rating", rating);
            if (comment!=null) {
                cuerpoPeticion.put("comment", comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, REST_API_BASE_URL +
                "/version/1/workers/" + id + "/comments",
                cuerpoPeticion,
                response -> {
                    // Don't forget to call the handler!
                    handler.workersCommentsRequestDidComplete();
                },
                new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String savedtoken= sharedPref.getString("token",null);
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("token", savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

    public void postAcceptWorkerRequest(int workerId, int requestId, PostAcceptWorkerRequestHandler handler) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                REST_API_BASE_URL + "/version/1/workers/" + workerId + "/requests/"+requestId,
                null,
                response -> handler.requestDidComplete(), new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());

                SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String savedtoken= sharedPref.getString("token",null);
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("token", savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }


    public void getWorkerRequestById(int id, GetWorkerRequestById handler) {

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                REST_API_BASE_URL + "/version/1/workers/" + id + "/requests",
                null,
                response -> {
                    // Here we decode the response.
                    // Dto objects make that easier for us.
                    List<JobRequestObjectDto> decodedWorkersList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject currentIterationElement = response.getJSONObject(i);
                            decodedWorkersList.add(new JobRequestObjectDto(currentIterationElement));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.requestDidFail(-1);
                        }
                    }
                    // Never forget to call the handler method!
                    handler.requestDidComplete(decodedWorkersList);
                },
                new DefaultErrorHandler(handler)
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>(super.getHeaders());
                SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String savedtoken= sharedPref.getString("token",null);
                // Añadimos la cabecera deseada
                if (savedtoken!=null) {
                    headers.put("token", savedtoken);
                }
                return headers;
            }
        };
        queue.add(request);
    }

}

