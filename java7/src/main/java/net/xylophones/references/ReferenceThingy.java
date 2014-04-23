package net.xylophones.references;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class ReferenceThingy {



    public static void main(String[] args) {
        Object obj = new Object();
        ReferenceQueue<Object> weakRefQueue = new ReferenceQueue();
        WeakReference<Object> weakRef = new WeakReference<>(obj, weakRefQueue);

    }



}
