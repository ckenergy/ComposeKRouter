import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeSpec
import java.util.Locale

/**
 * ckenergy  2022-11-11
 */
class NavGraphCreatorProxy(private val logBuilder: StringBuilder) {

    private val moduleMemberName = MemberName("com.ckenergy.compose.plugin.core", "composeModules")
    private val managerClassName = ClassName("com.ckenergy.compose.plugin.core", "NavGraphManager")
    private val providerClassName = ClassName("com.ckenergy.compose.plugin.core", "INavGraphProvider")
    private val getArguments = MemberName("com.ckenergy.compose.plugin.core", "getArguments")
    private val parseArguments = MemberName("com.ckenergy.compose.plugin.core", "parseArguments")

    /**
     * 创建Java代码
     *
     * @return
     */
    fun generateJavaCode(
        packageName: String,
        name: String,
        routerList: HashMap<KSFunctionDeclaration, ClassName?>
    ): TypeSpec {
        val graphName = "graph"
        val codeBlockBuilder =
            CodeBlock.Builder()
                .beginControlFlow(
                    "val $graphName = %M",
                    moduleMemberName
                )
                .addStatement("packageName = \"$packageName\"")
        routerList.forEach {
            val funDec = it.key
            val composePackageName = funDec.containingFile!!.packageName.asString()
            val funName = funDec.simpleName.asString()
            val routeName =
                funDec.annotations.firstOrNull { it1 ->
                    it1.annotationType.resolve().declaration.qualifiedName?.asString() == Constants.KEY_KROUTER_NAME
                }?.arguments?.find { it1 -> it1.name?.getShortName() == "routeName" }?.value.toString()

            val composeFunName = MemberName(composePackageName, funName)

            if (it.value == null) {
                codeBlockBuilder.beginControlFlow(
                    "composable(\"$routeName\")"
                )
                codeBlockBuilder.addStatement("%M()", composeFunName)
                codeBlockBuilder.endControlFlow()
            } else {
                val className = it.value!!
                val beanValueName =
                    className.simpleName.replaceFirstChar { it1 -> it1.lowercase(Locale.getDefault()) }
                val parameterStr = funDec.parameters.joinToString(separator = ",") { it1 ->
                    val valueName = it1.name!!.asString()
                    if (it1.hasDefault) {
                        log("$composePackageName.$funName function parameter $valueName set default value may not work")
                    }
                    "$valueName = ${beanValueName}." + valueName
                }

                codeBlockBuilder.beginControlFlow(
                    "composable(\"$routeName/{${Constants.KEY_ARG_NAME}}\", \n" +
                            "   arguments = %M()\n" +
                            ")", getArguments
                )
                codeBlockBuilder.addStatement(
                    "   val $beanValueName: %T? = it.%M()", className, parseArguments,
                )
                codeBlockBuilder.addStatement(
                    "   if($beanValueName == null) return@composable"
                )
                codeBlockBuilder.addStatement(
                    "   %M($parameterStr)", composeFunName
                )
                codeBlockBuilder.endControlFlow()
            }
        }
        codeBlockBuilder.endControlFlow()

        val managerName = "manager"

        val typeSpec = TypeSpec.objectBuilder(ClassName(packageName, name))
            .addFunction(
                FunSpec.builder("register")
                    .addParameter(managerName, managerClassName)
                    .addAnnotation(JvmStatic::class.java)
                    .addCode(codeBlockBuilder.addStatement("$managerName.addModules($graphName)").build())
                    .build()
            )
            .addSuperinterface(providerClassName)

        return typeSpec.build()
    }

    private fun log(msg: String) {
        logBuilder.append(msg + "\n")
    }

}