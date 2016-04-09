package com.juanmrivero.wearshoot.game;

import java.util.ArrayList;
import java.util.List;

public abstract class GameObjectPool<T extends GameObject> {

    private final List<T> objects;

    protected GameObjectPool(int poolSize) {
        objects = new ArrayList<>(poolSize);
        createObjects(poolSize);
    }

    private void createObjects(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            T object = createObject();
            object.active = false;
            objects.add(object);
        }
    }

    protected abstract T createObject();

    protected T getFirstInactive() {
        T inactiveObject = null;
        for (T object : objects) {
            if (!object.active) {
                inactiveObject = object;
                break;
            }
        }
        return inactiveObject;
    }

    public int getCount() {
        return objects.size();
    }

    public T get(int index) {
        return objects.get(index);
    }

    public void onUpdate(World world) {
        for (T object : objects) {
            object.onUpdate(world);
        }
    }

    public void reset() {
        for (T object : objects) {
            object.active = false;
        }
    }

}
