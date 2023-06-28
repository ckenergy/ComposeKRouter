package com.ckenergy.compose.kroute

import com.ckenergy.compose.kroute.utils.ScanSetting
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

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        list.forEach {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESTATIC, it.replace(".", "/"), ScanSetting.REGISTER_METHOD_NAME, "(L${ScanSetting.GENERATE_TO_CLASS_NAME};)V", false)
        }
    }

}