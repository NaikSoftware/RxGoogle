package ua.naiksoftware.rxgoogle;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.concurrent.TimeUnit;

import rx.SingleSubscriber;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class HistoryReadDailyTotalSingle extends BaseSingle<DataSet> {

    private final DataType dataType;

    HistoryReadDailyTotalSingle(RxGoogle rxFit, DataType dataType, Long timeout, TimeUnit timeUnit) {
        super(rxFit, timeout, timeUnit);
        this.dataType = dataType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final SingleSubscriber<? super DataSet> subscriber) {
        setupFitnessPendingResult(Fitness.HistoryApi.readDailyTotal(apiClient, dataType), new ResultCallback<DailyTotalResult>() {
            @Override
            public void onResult(@NonNull DailyTotalResult dailyTotalResult) {
                if (!dailyTotalResult.getStatus().isSuccess()) {
                    subscriber.onError(new StatusException(dailyTotalResult.getStatus()));
                } else {
                    subscriber.onSuccess(dailyTotalResult.getTotal());
                }
            }
        });
    }
}
