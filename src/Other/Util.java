package Other;

public class Util {
    public static void speak(String text) {
        try {
            String command = "cmd.exe /c echo " + text + " | PowerShell -Command \"Add-Type -AssemblyName System.Speech; " +
                    "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                    "$speak.Speak([Console]::In.ReadToEnd());\"";
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
