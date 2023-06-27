package com.ckenergy.compose.kroute

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input

interface CollectParams : InstrumentationParameters {

    @get:Input
    val registerMap: SetProperty<String>

}