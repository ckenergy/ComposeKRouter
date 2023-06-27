package com.ckenergy.compose.kroute

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.commons.AdviceAdapter

/**
 * Created by chengkai on 2023/1/19.
 */
class RegisterKRouteMethodVisitor(
    api: Int,
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String?,
    private val list: Set<String>
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {

    override fun onMethodEnter() {
        super.onMethodEnter()
        list.forEach {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESTATIC, it.replace(".", "/"), "register", "(Lcom/ckenergy/compose/plugin/core/NavGraphManager;)V", false)
        }

    }

}