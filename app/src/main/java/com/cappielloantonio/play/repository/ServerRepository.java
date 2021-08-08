package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.database.AppDatabase;
import com.cappielloantonio.play.database.dao.ServerDao;
import com.cappielloantonio.play.model.Server;

import java.util.List;

public class ServerRepository {
    private static final String TAG = "QueueRepository";

    private ServerDao serverDao;
    private LiveData<List<Server>> listLiveServer;

    public ServerRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        serverDao = database.serverDao();
    }

    public LiveData<List<Server>> getLiveServer() {
        listLiveServer = serverDao.getAll();
        return listLiveServer;
    }

    public void insert(Server server) {
        InsertThreadSafe insert = new InsertThreadSafe(serverDao, server);
        Thread thread = new Thread(insert);
        thread.start();
    }

    public void delete(Server server) {
        DeleteThreadSafe delete = new DeleteThreadSafe(serverDao, server);
        Thread thread = new Thread(delete);
        thread.start();
    }

    private static class InsertThreadSafe implements Runnable {
        private ServerDao serverDao;
        private Server server;

        public InsertThreadSafe(ServerDao serverDao, Server server) {
            this.serverDao = serverDao;
            this.server = server;
        }

        @Override
        public void run() {
            serverDao.insert(server);
        }
    }

    private static class DeleteThreadSafe implements Runnable {
        private ServerDao serverDao;
        private Server server;

        public DeleteThreadSafe(ServerDao serverDao, Server server) {
            this.serverDao = serverDao;
            this.server = server;
        }

        @Override
        public void run() {
            serverDao.delete(server);
        }
    }
}
