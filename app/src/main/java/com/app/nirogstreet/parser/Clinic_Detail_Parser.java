package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.ServicesModel;
import com.app.nirogstreet.model.SesstionModel;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.TimingsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 05-10-2017.
 */

public class Clinic_Detail_Parser {
    public static ArrayList<ClinicDetailModel> clinic_detail_parser(JSONObject message) {
        ArrayList<ClinicDetailModel> clinicDetailModels = new ArrayList<>();
        try {
            if (message.has("clinicsDetails") && !message.isNull("clinicsDetails")) {
                JSONArray jsonArrayclinicsDetails = message.getJSONArray("clinicsDetails");
                for (int i = 0; i < jsonArrayclinicsDetails.length(); i++) {

                    String clinicId = null;
                    String clinicName = null;
                    String clinicMobile = null;
                    String address = null;
                    String state = null;
                    String clinicCity = null;
                    String pincode = null;
                    String at_lat = null;
                    String created_by=null;
                    String at_long = null;
                    String clinic_docID=null;
                    String consultation_fee = null;
                    ArrayList<SpecializationModel> servicesModels = new ArrayList<>();

                    JSONObject jsonObject = jsonArrayclinicsDetails.getJSONObject(i);
                    if (jsonObject.has("consultation_fee") && !jsonObject.isNull("consultation_fee")) {
                        consultation_fee = jsonObject.getString("consultation_fee");
                    }
                    if(jsonObject.has("clinic_docID")&&!jsonObject.isNull("clinic_docID"))
                    {
                        clinic_docID=jsonObject.getString("clinic_docID");
                    }
                    if (jsonObject.has("clinicDetail") && !jsonObject.isNull("clinicDetail")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("clinicDetail");

                        if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                            clinicId = jsonObject1.getString("id");
                        }
                        if (jsonObject1.has("name") && !jsonObject1.isNull("name")) {
                            clinicName = jsonObject1.getString("name");
                        }
                        if (jsonObject1.has("mobile") && !jsonObject1.isNull("mobile")) {
                            clinicMobile = jsonObject1.getString("mobile");
                        }
                        if (jsonObject1.has("address") && !jsonObject1.isNull("address")) {
                            address = jsonObject1.getString("address");
                        }
                        if (jsonObject1.has("state") && !jsonObject1.isNull("state")) {
                            state = jsonObject1.getString("state");
                        }
                        if (jsonObject1.has("city") && !jsonObject1.isNull("city")) {
                            clinicCity = jsonObject1.getString("city");
                        }
                        if (jsonObject1.has("pincode") && !jsonObject1.isNull("pincode")) {
                            pincode = jsonObject1.getString("pincode");
                        }
                        if (jsonObject1.has("at_lat") && !jsonObject1.isNull("at_lat")) {
                            at_lat = jsonObject1.getString("at_lat");
                        }
                        if (jsonObject1.has("at_long") && !jsonObject1.isNull("at_long")) {
                            at_long = jsonObject1.getString("at_long");
                        }if(jsonObject1.has("created_by")&&!jsonObject1.isNull("created_by"))
                        {
                            created_by=jsonObject1.getString("created_by");
                        }
                        servicesModels = ServicesParser.serviceParser(jsonObject1);

                    }
                    ArrayList<TimingsModel> timingsModels1 = new ArrayList<>();
                    timingsModels1.add(new TimingsModel("Mon", new SesstionModel("", "","","",false), new SesstionModel("", "","","",false)));
                    timingsModels1.add(new TimingsModel("Tue", new SesstionModel("", "","","",false), new SesstionModel("", "","","",false)));

                    timingsModels1.add(new TimingsModel("Wed", new SesstionModel("", "","","",false), new SesstionModel("", "","","",false)));

                    timingsModels1.add(new TimingsModel("Thu", new SesstionModel("", "","","",false), new SesstionModel("", "","","",false)));

                    timingsModels1.add(new TimingsModel("Fri", new SesstionModel("", "","","",false), new SesstionModel("", "","","",false)));

                    timingsModels1.add(new TimingsModel("Sat", new SesstionModel("", "","","",false), new SesstionModel("", "","","",false)));
                    timingsModels1.add(new TimingsModel("Sun", new SesstionModel("", "","","",false), new SesstionModel("", "","","",false)));
                    if (jsonObject.has("timeSlots") && !jsonObject.isNull("timeSlots")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("timeSlots");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            String start_time = null, end_time = null, days = null, sesstion = null;
                            SesstionModel sesstionModel;
                            if (jsonObject1.has("start_time") && !jsonObject1.isNull("start_time")) {
                                start_time = jsonObject1.getString("start_time");

                            }
                            if (jsonObject1.has("end_time") && !jsonObject1.isNull("end_time")) {
                                end_time = jsonObject1.getString("end_time");

                            }
                            if (jsonObject1.has("days") && !jsonObject1.isNull("days")) {
                                days = jsonObject1.getString("days");

                            }
                            if (jsonObject1.has("session") && !jsonObject1.isNull("session")) {
                                sesstion = jsonObject1.getString("session");

                            }
                            sesstionModel = new SesstionModel(start_time, end_time, days, sesstion,true);
                            if (days.equalsIgnoreCase("Monday")) {
                                if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(0).setSesstionModel1(sesstionModel);
                                else if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(0).setSesstionModel2(sesstionModel);

                            }
                            if (days.equalsIgnoreCase("Tuesday")) {
                                if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(1).setSesstionModel1(sesstionModel);
                                else if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(1).setSesstionModel2(sesstionModel);

                            }
                            if (days.equalsIgnoreCase("Wednesday")) {
                                if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(2).setSesstionModel1(sesstionModel);
                                else if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(2).setSesstionModel2(sesstionModel);

                            }
                            if (days.equalsIgnoreCase("Thursday")) {
                                if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(3).setSesstionModel1(sesstionModel);
                                else if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(3).setSesstionModel2(sesstionModel);

                            }
                            if (days.equalsIgnoreCase("Friday")) {
                                if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(4).setSesstionModel1(sesstionModel);
                                else if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(4).setSesstionModel2(sesstionModel);

                            }
                            if (days.equalsIgnoreCase("Saturday")) {
                                if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(5).setSesstionModel1(sesstionModel);
                                else if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(5).setSesstionModel2(sesstionModel);

                            }
                            if (days.equalsIgnoreCase("Sunday")) {
                                if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(6).setSesstionModel1(sesstionModel);
                                else if (sesstion.equalsIgnoreCase("Morning"))
                                    timingsModels1.get(6).setSesstionModel2(sesstionModel);

                            }
                        }
                    }
                    clinicDetailModels.add(new ClinicDetailModel(clinicId, clinicName, clinicMobile, address, state, clinicCity, pincode, at_lat, at_long, consultation_fee, servicesModels, timingsModels1,created_by,clinic_docID));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clinicDetailModels;
    }
}
