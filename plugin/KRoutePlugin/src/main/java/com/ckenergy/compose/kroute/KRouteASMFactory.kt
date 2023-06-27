package com.ckenergy.compose.kroute

import com.android.build.api.instrumentation.*
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes


abstract class KRouteASMFactory: AsmClassVisitorFactory<CollectParams> {
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return RegisterKRouteClassVisitor(Opcodes.ASM7, nextClassVisitor, parameters.get().registerMap.get())
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return parameters.get().registerMap.get().isNotEmpty() && classData.className == "com.ckenergy.compose.plugin.core.NavGraphManager"
    }
}
