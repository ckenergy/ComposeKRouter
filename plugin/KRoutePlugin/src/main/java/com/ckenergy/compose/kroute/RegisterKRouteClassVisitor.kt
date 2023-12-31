package com.ckenergy.compose.kroute

import com.ckenergy.compose.kroute.utils.ScanSetting
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * Created by chengkai on 2023/1/19.
 */
class RegisterKRouteClassVisitor(api: Int, classVisitor: ClassVisitor?, private val list: Set<String>) :
    ClassVisitor(api, classVisitor) {

    private var superName: String? = null
    private var className: String? = null


    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.superName = superName
        className = name
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

        if (className == ScanSetting.GENERATE_TO_CLASS_NAME && name == ScanSetting.GENERATE_TO_METHOD_NAME) {
            println("RegisterKRouteClassVisitor:$list")
            return RegisterKRouteMethodVisitor(api, mv, access, name, descriptor, list)
        }
        return mv
    }

}