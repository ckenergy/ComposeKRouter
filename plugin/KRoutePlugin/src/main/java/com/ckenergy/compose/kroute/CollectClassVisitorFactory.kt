package com.ckenergy.compose.kroute

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

abstract class CollectClassVisitorFactory : AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return nextClassVisitor
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        if (classData.className.contains("NavGraph")) {
            classData.interfaces.find {
                it == "com.ckenergy.compose.plugin.core.INavGraphProvider"
            }?.run {
                println("CollectClassVisitorFactory:${classData.className}")
                KRoutePlugin.graphList.add(classData.className)
            }
            return true
        }
        return false
    }
}