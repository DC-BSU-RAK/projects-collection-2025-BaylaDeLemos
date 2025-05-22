package com.example.chordtransposer

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // UI component declarations
    private lateinit var inputChords: EditText
    private lateinit var inputShift: EditText
    private lateinit var outputChords: TextView
    private lateinit var btnTranspose: Button
    private lateinit var btnModal: Button
    private lateinit var spinnerTargetKey: Spinner

    // Chords in sharp notation (used as standard reference)
    private val sharpChords = listOf(
        "C", "C#", "D", "D#", "E", "F",
        "F#", "G", "G#", "A", "A#", "B"
    )

    // Mapping from flat to sharp chord names
    private val flatToSharpMap = mapOf(
        "DB" to "C#",
        "EB" to "D#",
        "GB" to "F#",
        "AB" to "G#",
        "BB" to "A#"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputChords = findViewById(R.id.input_chords)
        inputShift = findViewById(R.id.input_shift)
        outputChords = findViewById(R.id.output_chords)
        btnTranspose = findViewById(R.id.btn_transpose)
        btnModal = findViewById(R.id.btn_modal)
        spinnerTargetKey = findViewById(R.id.spinner_target_key)

// Spinner selection listener
        spinnerTargetKey.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = spinnerTargetKey.selectedItem.toString()
                if (!selected.equals("None", ignoreCase = true)) {
                    inputShift.setText("")
                    inputShift.isEnabled = false
                } else {
                    inputShift.isEnabled = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

// Input shift text watcher
        inputShift.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && spinnerTargetKey.selectedItemPosition != 0) {
                spinnerTargetKey.setSelection(0)
            }
        }

        inputShift.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrBlank()) {
                    spinnerTargetKey.setSelection(0)
                    spinnerTargetKey.isEnabled = false
                } else {
                    spinnerTargetKey.isEnabled = true
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Handle transpose button click
        btnTranspose.setOnClickListener {
            val chordInput = inputChords.text.toString().trim()
            if (chordInput.isEmpty()) {
                Toast.makeText(this, "Please enter chords", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Try to get selected target key or numeric shift
            val targetKeyRaw = spinnerTargetKey.selectedItem.toString()
            val targetKey = normalizeKeyName(targetKeyRaw)
            val shiftInput = inputShift.text.toString().toIntOrNull()

            // Determine transpose method based on user input
            val result = when {
                targetKey != null -> {
                    val transposed = transposeToTargetKey(chordInput, targetKey)
                    formatOutput(chordInput, transposed)
                }
                shiftInput != null -> {
                    val transposed = transposeByShift(chordInput, shiftInput)
                    formatOutput(chordInput, transposed)
                }
                else -> {
                    Toast.makeText(this, "Please enter a valid shift or select a target key", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            outputChords.text = result
        }

        // Show instruction modal dialog
        btnModal.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_instructions, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            dialogView.findViewById<Button>(R.id.close_modal_btn)?.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    // Normalize the key name from spinner input
    private fun normalizeKeyName(rawKey: String): String? {
        return when {
            rawKey.equals("None", ignoreCase = true) -> null
            rawKey.contains("/") -> rawKey.split("/")[0].trim()
            else -> rawKey.trim()
        }
    }

    // Convert flat note to equivalent sharp note
    private fun toSharp(note: String): String {
        val upper = note.uppercase()
        return flatToSharpMap[upper] ?: upper
    }

    // Extract root note from a chord string (e.g., "C#m7" → "C#")
    private fun extractRootNote(chord: String): String? {
        if (chord.isEmpty()) return null
        val c = chord[0].uppercaseChar()
        return if (chord.length > 1) {
            val second = chord[1].uppercaseChar()
            if (second == '#' || second == 'B') {
                c.toString() + second
            } else {
                c.toString()
            }
        } else {
            c.toString()
        }
    }

    // Transpose a single chord by a number of semitones (shift)
    private fun transposeChord(chord: String, shift: Int): String {
        val rootNote = extractRootNote(chord) ?: return chord
        val suffix = chord.substring(rootNote.length) // Preserve minor/7/etc
        val rootSharp = toSharp(rootNote)
        val index = sharpChords.indexOf(rootSharp)

        return if (index != -1) {
            val newIndex = (index + shift + sharpChords.size) % sharpChords.size
            sharpChords[newIndex] + suffix
        } else {
            chord
        }
    }

    // Transpose a space-separated list of chords by shift
    private fun transposeByShift(input: String, shift: Int): List<String> {
        return input.split(" ").map { chord -> transposeChord(chord, shift) }
    }

    // Transpose chords from original key to a specific target key
    private fun transposeToTargetKey(input: String, targetKey: String): List<String> {
        val chordsList = input.split(" ")
        if (chordsList.isEmpty()) return emptyList()

        val firstChord = chordsList[0]
        val firstRoot = extractRootNote(firstChord) ?: return chordsList

        val firstRootSharp = toSharp(firstRoot)
        val targetSharp = toSharp(targetKey)

        val baseIndex = sharpChords.indexOf(firstRootSharp)
        val targetIndex = sharpChords.indexOf(targetSharp)

        if (baseIndex == -1 || targetIndex == -1) return chordsList

        val shift = (targetIndex - baseIndex + sharpChords.size) % sharpChords.size
        return chordsList.map { chord -> transposeChord(chord, shift) }
    }

    // Build a major scale chord family for the given key
    private fun getChordFamily(key: String): List<String> {
        val majorScaleSteps = listOf(2, 2, 1, 2, 2, 2, 1) // Whole & half steps
        val chordTypes = listOf("", "m", "m", "", "", "m", "dim")

        val startNote = toSharp(key)
        val scale = mutableListOf<String>()
        var index = sharpChords.indexOf(startNote)
        if (index == -1) return listOf("Unknown key")

        scale.add(sharpChords[index])
        for (step in majorScaleSteps.dropLast(1)) {
            index = (index + step) % sharpChords.size
            scale.add(sharpChords[index])
        }

        return scale.zip(chordTypes).map { (note, suffix) -> note + suffix }
    }

    // Format the final output nicely
    private fun formatOutput(originalInput: String, transposed: List<String>): String {
        val originalChords = originalInput.split(" ")
        val originalRoot = extractRootNote(originalChords[0]) ?: return "Invalid original chord"
        val transposedRoot = extractRootNote(transposed[0]) ?: return "Invalid transposed chord"

        val originalFamily = getChordFamily(originalRoot)
        val transposedFamily = getChordFamily(transposedRoot)

        return """
            🎵 Original Key: ${toSharp(originalRoot)}
            Family: ${originalFamily.joinToString(" ")}

            🎵 Transposed Key: ${toSharp(transposedRoot)}
            Family: ${transposedFamily.joinToString(" ")}

            🎼 Input Chords:
            ${originalChords.joinToString(", ")}

            🔁 Transposed Chords:
            ${transposed.joinToString(", ")}
        """.trimIndent()
    }
}
