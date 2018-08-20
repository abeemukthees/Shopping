package com.abeemukthees.domain.utilities

import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.Subject
import io.reactivex.subscribers.DisposableSubscriber
import org.reactivestreams.Processor

object RxUtils {

    val trimEndFunction = { charSequence: CharSequence -> charSequence.toString().replace("\\s+$".toRegex(), "") }

    operator fun <D> get(subject: Subject<D>): DisposableObserver<D> {
        return object : DisposableObserver<D>() {
            override fun onNext(d: D) {
                subject.onNext(d)
            }

            override fun onError(throwable: Throwable) {
                subject.onError(throwable)
                throwable.printStackTrace()
            }

            override fun onComplete() {
                subject.onComplete()
            }
        }
    }

    operator fun <D, E> get(subject: Processor<D, E>): DisposableSubscriber<D> {
        return object : DisposableSubscriber<D>() {
            override fun onNext(d: D) {
                subject.onNext(d)
            }

            override fun onError(throwable: Throwable) {
                subject.onError(throwable)
                throwable.printStackTrace()
            }

            override fun onComplete() {
                subject.onComplete()
            }
        }
    }
}
