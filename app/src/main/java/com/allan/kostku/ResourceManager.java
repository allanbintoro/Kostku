package com.allan.kostku;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.allan.kostku.ActivityAdminKost.AdminDashboard;
import com.allan.kostku.ActivityMaster.MasterDashboard;
import com.allan.kostku.ActivityUser.MainActivity;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.Model.Room;
import com.allan.kostku.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;


public class ResourceManager {
    public static User LOGGED_USER = null;
    public static Room MYROOM = null;
    public static ArrayList<Kost> KOSTS;//Master
    public static ArrayList<Kost> KOSTSUID;//Admin
    public static ArrayList<Room> ROOMS;//Master
    public static ArrayList<Room> KOSTROOM;//Master
    public static ArrayList<User> USERS;
    public static ArrayList<String> USERS_LIST;

    public static SharedPreferences mPref;
    public static final String PARAM_INTENT_DATA = "Intent Data";
    public static Boolean isLoadKost = false, isLoadUser = false, isLoadUserList = false,
            isLoadRoom = false;
    public static FirebaseFunctions FUNCTIONS = FirebaseFunctions.getInstance();


    public static void logUser(final Activity activity) {
        mPref = activity.getSharedPreferences("userPref", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = mPref.getString("loggedUser", "");

        User user = gson.fromJson(json, User.class);

        ResourceManager.LOGGED_USER = user;

        if (LOGGED_USER == null) return;
        if (LOGGED_USER.getUserType().equalsIgnoreCase("1")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    Log.e("newToken", newToken);
                }
            });
            String topic = "Master";
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            Intent intent = new Intent(activity, MasterDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            activity.finish();
            activity.startActivity(intent);
        } else if (LOGGED_USER.getUserType().equalsIgnoreCase("2")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    Log.e("newToken", newToken);
                }
            });
            String topic = "Owner";
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            Intent intent = new Intent(activity, AdminDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            activity.finish();
            activity.startActivity(intent);
        } else if (LOGGED_USER.getUserType().equalsIgnoreCase("3")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
                    Log.e("newToken", newToken);
                }
            });
            String topic = ResourceManager.LOGGED_USER.getUserId();
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
            activity.finish();
            activity.startActivity(intent);
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code
                loadRoom(activity);
                loadKost(activity);
                loadUser(activity);
                loadUserList(activity);
                loadKostByUid(activity);
            }
        });
    }

    public static void saveLocalUser(User user, Activity activity) {
        mPref = activity.getSharedPreferences("userPref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mPref.edit().putString("loggedUser", json).commit();
        mPref.edit().apply();
        String test = mPref.getString("loggedUser", "");

        ResourceManager.logUser(activity);
    }

    public static void logOutUser(Context context) {
        if (LOGGED_USER.getUserType().equalsIgnoreCase("1")) {
            String topic = "Master";
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        } else if (LOGGED_USER.getUserType().equalsIgnoreCase("2")) {
            String topic = "Admin";
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        } else if (LOGGED_USER.getUserType().equalsIgnoreCase("3")) {
            String topic = ResourceManager.LOGGED_USER.getUserId();
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }
        FirebaseAuth.getInstance().signOut();
        mPref = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
        mPref.edit().clear().apply();
        mPref.edit().commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void loadKost(final Activity activity) {
        KOSTS = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Kost")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("EMenu", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                            Kost tempKost = dc.getDocument().toObject(Kost.class);
                            int index = -1;
                            switch (dc.getType()) {
                                case ADDED:
                                    KOSTS.add(tempKost);
                                    Log.d("EMenu", "Added: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    index = getKostIndexByUID(KOSTS, tempKost.getKostId());
                                    if (index >= 0) {
                                        KOSTS.set(index, tempKost);
                                    }
                                    Log.d("EMenu", "Modified: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    index = getKostIndexByUID(KOSTS, tempKost.getKostId());
                                    if (index >= 0) {
                                        KOSTS.remove(index);
                                    }
                                    Log.d("EMenu", "Removed: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                        isLoadKost = true;
                        sendBroadcast(activity);
//                        goToMainActivity(activity);

                    }
                });
    }

    public static void loadKostByUid(final Activity activity) {
        KOSTSUID = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Kost")
                .whereEqualTo("kostOwnerId", LOGGED_USER.getUserId())
                .orderBy("kostName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("EMenu", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                            Kost tempKost = dc.getDocument().toObject(Kost.class);
                            int index = -1;
                            switch (dc.getType()) {
                                case ADDED:
                                    KOSTSUID.add(tempKost);
                                    Log.d("EMenu", "Added: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    index = getKostIndexByUID(KOSTSUID, tempKost.getKostId());
                                    if (index >= 0) {
                                        KOSTSUID.set(index, tempKost);
                                    }
                                    Log.d("EMenu", "Modified: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    index = getKostIndexByUID(KOSTSUID, tempKost.getKostId());
                                    if (index >= 0) {
                                        KOSTSUID.remove(index);
                                    }
                                    Log.d("EMenu", "Removed: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                        isLoadKost = true;
                        sendBroadcast(activity);

                    }
                });
    }

    public static void loadRoom(final Activity activity) {
        ROOMS = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collectionGroup("Room")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("EMenu", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                            Room tempRoom = dc.getDocument().toObject(Room.class);
                            int index = -1;
                            switch (dc.getType()) {
                                case ADDED:
                                    ROOMS.add(tempRoom);
                                    Log.d("EMenu", "Added: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    index = getRoomIndexByUID(ROOMS, tempRoom.getRoomId());
                                    if (index >= 0) {
                                        ROOMS.set(index, tempRoom);
                                    }
                                    Log.d("EMenu", "Modified: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    index = getRoomIndexByUID(ROOMS, tempRoom.getRoomId());
                                    if (index >= 0) {
                                        ROOMS.remove(index);
                                    }
                                    Log.d("EMenu", "Removed: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                        Log.e("onEvent: ", ResourceManager.ROOMS + "");

                        isLoadRoom = true;
                        sendBroadcast(activity);
                    }
                });
    }

    public static void loadUser(final Activity activity) {
        USERS = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User")
                .orderBy("userType", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("EMenu", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                            User tempUser = dc.getDocument().toObject(User.class);
                            int index = -1;
                            switch (dc.getType()) {
                                case ADDED:
                                    USERS.add(tempUser);
                                    Log.d("EMenu", "Added: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    index = getUserIndexByUID(USERS, tempUser.getUserId());
                                    if (index >= 0) {
                                        USERS.set(index, tempUser);
                                    }
                                    Log.d("EMenu", "Modified: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    index = getUserIndexByUID(USERS, tempUser.getUserId());
                                    if (index >= 0) {
                                        USERS.remove(index);
                                    }
                                    Log.d("EMenu", "Removed: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                        isLoadUser = true;
                        sendBroadcast(activity);
                    }
                });
    }

    public static void sendBroadcast(Activity activity) {

        if (isLoadKost && isLoadRoom && isLoadUser) {
            Intent intent = new Intent("loadComplete");
            Log.e("Send Broadcast", "Done");
            LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        }
    }

    public static void loadUserList(Activity activity) {
        USERS_LIST = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User")
                .whereEqualTo("userType", "2")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            User user = documentSnapshot.toObject(User.class);
                            Log.e("userList: ", user + "");

                            USERS_LIST.add(user.getUserName());
                        }

                    }
                });
        isLoadUserList = true;
        sendBroadcast(activity);
    }

    public static ArrayList<Room> getRoomByKostId(ArrayList<Room> rooms, String kostId) {
        ArrayList<Room> kostRoom = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getKostId().equals(kostId)) {
                kostRoom.add(rooms.get(i));
            }
        }
        return kostRoom;
    }

    public static Boolean checkRoomName(ArrayList<Room> rooms, String kostId, String roomName) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomName().equals(roomName)) {
                if (rooms.get(i).getKostId().equals(kostId)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Boolean updateRoomNamesameId(ArrayList<Room> rooms, String roomId, String roomName) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomName().equals(roomName)) {
                if (rooms.get(i).getRoomId().equals(roomId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getUserByName(ArrayList<User> users, String userName) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserName().equals(userName)) {
                return users.get(i).getUserId();
            }
        }
        return null;
    }
    public static String getUserNameById(ArrayList<User> users, String uId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(uId)) {
                return users.get(i).getUserName();
            }
        }
        return null;
    }

    public static ArrayList<String> getKostName(ArrayList<Kost> kosts) {
        ArrayList<String> kostName = new ArrayList<>();
        for (int i = 0; i < kosts.size(); i++) {
            kostName.add(kosts.get(i).getKostName());
        }
        return kostName;
    }

    public static ArrayList<String> getKostNameById(ArrayList<Kost> kosts, String kostOwnerId) {
        ArrayList<String> kostName = new ArrayList<>();
        for (int i = 0; i < kosts.size(); i++) {
            if (kostOwnerId.equals(kosts.get(i).getKostOwnerId())) {
                kostName.add(kosts.get(i).getKostName());
            }
        }
        return kostName;
    }

    public static ArrayList<String> getRoomList(ArrayList<Room> rooms, String kostId) {
        ArrayList<String> roomList = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getKostId().equals(kostId)) {
                roomList.add(rooms.get(i).getRoomName());
            }
        }
        return roomList;
    }

    //Collect All Room In Kost
    public static ArrayList<String> getRoomByKostName(ArrayList<Room> rooms, String kostId) {
        ArrayList<String> roomList = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getKostId().equals(kostId)) {
                if (rooms.get(i).isRoomStatus()) {
                    roomList.add(rooms.get(i).getRoomName());
                }
            }
        }
        return roomList;
    }

    public static String getRoomByName(ArrayList<Room> rooms, String kostId, String roomName) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getKostId().equals(kostId)) {
                if (rooms.get(i).getRoomName().equals(roomName)) {
                    return rooms.get(i).getRoomId();
                }
            }
        }
        return null;
    }

    public static Room getRoomData(ArrayList<Room> rooms, String roomId) {
        Room a = new Room();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomId().equals(roomId)) {

                return a = rooms.get(i);

            }
        }
        return null;
    }

    public static String getKostIdByKostName(ArrayList<Kost> kosts, String kostName) {
        for (int i = 0; i < kosts.size(); i++) {
            if (kosts.get(i).getKostName().equals(kostName)) {
                return kosts.get(i).getKostId();
            }
        }
        return null;
    }

    public static String getUserByUID(ArrayList<User> users, String uid) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(uid)) {
                return users.get(i).getUserName();
            }
        }
        return null;
    }

    public static String getKostNameByUid(ArrayList<Kost> kosts, String kostId) {
        for (int i = 0; i < kosts.size(); i++) {
            if (kostId.equals(kosts.get(i).getKostId())) {
                return kosts.get(i).getKostName();
            }
        }
        return null;
    }

    public static Room getUserRoom(ArrayList<Room> rooms, String uid) {
        Room a = new Room();
        for (int i = 0; i < rooms.size(); i++) {
            if (uid.equals(rooms.get(i).getUserId())) {
                return a = rooms.get(i);
            }
        }
        return null;
    }
    public static Room getUserRoomByRoomId(ArrayList<Room> rooms, String roomId) {
        Room a = new Room();
        for (int i = 0; i < rooms.size(); i++) {
            if (roomId.equals(rooms.get(i).getRoomId())) {
                return a = rooms.get(i);
            }
        }
        return null;
    }

    public static ArrayList<String> getKostList(ArrayList<Kost> kosts, String ownerId) {
        ArrayList<String> kostList = new ArrayList<>();
        for (int i = 0; i < kosts.size(); i++) {
            if (kosts.get(i).getKostOwnerId().equals(ownerId)) {
                kostList.add(kosts.get(i).getKostId());
            }
        }
        return kostList;
    }

    //Get All UserID from RoomList
    public static ArrayList<String> getUsersListByUid(ArrayList<Room> rooms, ArrayList<String> kostId) {
        ArrayList<String> userList = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = 0; j < kostId.size(); j++) {
                if (rooms.get(i).getKostId().equals(kostId.get(j))) {
                    userList.add(rooms.get(i).getUserId());
                }
            }
        }
        return userList;
    }

    //User Object From getUserListByUid
    public static ArrayList<User> getUserByUIDRoom(ArrayList<User> users, ArrayList<String> userId) {
        ArrayList<User> userList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            for (int j = 0; j < userId.size(); j++) {
                if (users.get(i).getUserId().equals(userId.get(j))) {
                    userList.add(users.get(i));
                }
            }
        }
        return userList;
    }


    private static int getKostIndexByUID(ArrayList<Kost> kosts, String uid) {
        for (int i = 0; i < kosts.size(); i++) {
            if (kosts.get(i).getKostId().equals(uid)) {
                return i;
            }
        }
        return -1;
    }

    private static int getRoomIndexByUID(ArrayList<Room> rooms, String uid) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomId().equals(uid)) {
                return i;
            }
        }
        return -1;
    }

    private static int getUserIndexByUID(ArrayList<User> users, String uid) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equalsIgnoreCase(uid)) {
                return i;
            }
        }
        return -1;
    }

    public static String currencyFormatter(double val) {
        NumberFormat kursRupiah = (NumberFormat) NumberFormat.getInstance();
        kursRupiah.setMaximumFractionDigits(0);

        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setGroupingSeparator('.');

        return "Rp " + kursRupiah.format(val).replaceAll(",", ".");
    }
    

}
