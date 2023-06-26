import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * ckenergy  2022-11-11
 */
class ParameterCreatorProxy(private val logBuilder: StringBuilder) {

    /**
     * 创建Java代码
     *
     * @return
     */
    fun generateJavaCode(
        name: String,
        parameters: List<KSValueParameter>
    ): TypeSpec {
        val funBuilder = FunSpec.constructorBuilder()
        parameters.forEach {
            val list = it.type.modifiers.mapNotNull { it1 -> it1.toKModifier() }
            funBuilder.addParameter(it.name!!.asString(), it.type.toTypeName(), list)
        }
        val bindingClass = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.PUBLIC)
            .primaryConstructor(funBuilder.build())
        parameters.forEach {
            bindingClass.addProperty(
                PropertySpec.builder(
                    it.name!!.asString(),
                    it.type.toTypeName()
                ).initializer(it.name!!.asString()).build()
            )
        }

        return bindingClass.build()
    }

    private fun log(msg: String) {
        logBuilder.append(msg + "\n")
    }

}