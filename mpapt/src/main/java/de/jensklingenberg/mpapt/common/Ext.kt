package de.jensklingenberg.mpapt.common

import de.jensklingenberg.mpapt.model.Platform
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.constants.KClassValue


fun MessageCollector.warn(s: String) {
    report(
            CompilerMessageSeverity.WARNING,
            s
    )
}


//TODO: Find more reliable way
fun CompilerConfiguration.guessingPlatform(): Platform {
    val JS_STRING = "JSCompiler"
    val JVM = "JVMCompiler"
    val NATIVE ="K2NativeCompilerPerformanceManager"
    val perf:String= this.get(CLIConfigurationKeys.PERF_MANAGER).toString()?:""

    return if(perf.contains(JS_STRING)){
        Platform.JS
    }else if(perf.contains(JVM)){
        Platform.JVM
    }
    else if(perf.contains(NATIVE)){
        Platform.NATIVE
    }
    else{
        Platform.UNKNONWN
    }
}


fun MessageCollector.printMessage(diagnosticKind: DiagnosticKind,message:String) {
    when(diagnosticKind){
        DiagnosticKind.WARNING->{
            this.report( CompilerMessageSeverity.WARNING,
                    message,
                    CompilerMessageLocation.create(null))
        }
        DiagnosticKind.ERROR->{
            this.report( CompilerMessageSeverity.ERROR,
                    message,
                    CompilerMessageLocation.create(null))
        }
        DiagnosticKind.MANDATORY_WARNING->{
            this.report( CompilerMessageSeverity.STRONG_WARNING,
                    message,
                    CompilerMessageLocation.create(null))
    }
        DiagnosticKind.NOTE->{
            this.report( CompilerMessageSeverity.INFO,
                    message,
                    CompilerMessageLocation.create(null))
        }
        DiagnosticKind.LOG->{
            this.report( CompilerMessageSeverity.LOGGING,
                    message,
                    CompilerMessageLocation.create(null))
        }
        else->{
            this.report( CompilerMessageSeverity.WARNING,
                    message,
                    CompilerMessageLocation.create(null))
        }
    }

}


fun KClassValue.getVariableNames(moduleDescriptor: ModuleDescriptor): List<Name> =
        this.getArgumentType(moduleDescriptor).memberScope.getVariableNames().toList()



enum class DiagnosticKind{
    ERROR,
    WARNING,
    MANDATORY_WARNING,
    NOTE,
    LOG,
    OTHER;
}