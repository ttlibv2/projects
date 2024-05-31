package vn.conyeu.javafx.controls;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.Objects;
import vn.conyeu.javafx.controls.skin.MaskFieldSkin;
import vn.conyeu.javafx.styleable.CssFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Slf4j
public class MaskField extends javafx.scene.control.TextField {
    private ObservableMap<Character, MaskVerifierCb> maskVerifiers;// = FXCollections.observableHashMap();
    private ObservableMap<Character, ConversionCb> conversions;// = FXCollections.observableHashMap();
    private String fixedText;

    public MaskField() {
        this("");
    }

    public MaskField(String text) {
        super(text);
        getStyleClass().add("mask-field");
        initializeListener();
        focusedProperty().addListener(e -> {
            final String mask = getInitialTextFromMask();
            final String fieldText = getText();


            if(!isFocused() ) {
                if(fieldText.equals(mask)) setText("");
            }
            else if(fieldText.isBlank()) {
                setText(mask);
            }

            //int pos = firstPlaceholderPosition();
            //selectRange(pos, pos);
            positionCaret(0);
        });
    }

    public final ObservableMap<Character, MaskVerifierCb> getMaskVerifiers() {
        if(maskVerifiers == null) {
            maskVerifiers = FXCollections.observableHashMap();
            initializeMaskVerifiers(maskVerifiers);
        }
        return maskVerifiers;
    }

    public final ObservableMap<Character, ConversionCb> getConversions() {
        if(conversions == null) {
            conversions= FXCollections.observableHashMap();
            initializeConversions(conversions);
        }
        return conversions;
    }

    public void initializeDefaultText() {
        deselect();
        if (!textProperty().isBound()) {
            setText(getInitialText() != null ? getInitialText() : getInitialTextFromMask());
        }
    }

    @Override
    public void selectRange(int anchor, int caretPosition) {
        super.selectRange(anchor, caretPosition);
    }


    public void replaceText(int start, int end, String text) {
        String inputMask = getInputMask();
        if (Objects.isBlank(inputMask)) {
            super.replaceText(start, end, text);
        }//
        else {

            int index = 0;
            int count = end - start;
            int caret = getCaretPosition();

            int caretStart = caret;

            // don´t accept user input space
            if (text.equals(" ")) {
                return;
            }

            String existingText = getText();
            String deletedText = existingText.substring(start, end);
            String newText = keepFixedText(deletedText);

            super.replaceText(start, end, newText);

            while (start < getInputMask().length()) {
                if (index >= text.length()) break;

                char c = text.charAt(index); // get the char from the text.
                if (verifyChar(c, start)) {
                    String cc = String.valueOf(convert(c, start));
                    int endT = getText().length() > start ? start + 1 : start;
                    super.replaceText(start, endT, cc);

                    index++; // move to the next char in the text
                    start++; // move to the next caret position
                    count--; // counting how many char we replaced, and we just did one char here.
                } //
                else {
                    // auto-advance
                    if (isAutoAdvance() && (
                            getFixedText().contains(String.valueOf(c)) ||
                                    (caret < existingText.length() && existingText.charAt(caret) != getPlaceholderCharacter() && getFixedText().contains(String.valueOf(existingText.charAt(caret)))))) {
                        start++; // move to the next caret position
                        caret++;
                        selectRange(caret, caret);
                    } else {
                        break;
                    }

                    // Fix mask have space and caret is in this position
                    while (Character.isSpaceChar(inputMask.charAt(start))) {
                        start++;
                    }

                    this.replaceText(start, start, text);

                }
                // only break when there is no new text and we are done with the count that we are expected to do
                // if there are still new text, we will continue even when the count is done
                if (count <= 0 && index >= text.length()) {
                    break;
                }
            }

            // caret go to last position all times
            selectRange(start, start);

            // this code solution -> When user select all text and caret is in end line, text is lost
            if ((caretStart == inputMask.length()) && (start == 0) && (!text.isEmpty()) && (caretStart == caret) && (caretStart == end)) {
                this.replaceText(start, start, text);
            }
        }
    }

    public void replaceSelection(String replacement) {
        IndexRange range = getSelection();
        if (getInputMask() != null) {
            String newText = keepFixedText(getSelectedText());
            super.replaceText(range.getStart(), range.getEnd(), newText);

            selectRange(range.getStart(), range.getStart());
            for (int i = 0; i < replacement.length(); i++) {
                int start = getCaretPosition();
                replaceText(start, start, String.valueOf(replacement.charAt(i)));
            }
        } else {
            super.replaceSelection(replacement);
        }
    }


    //@formatter:off

    /**
     * the placeholder character. This is used fill in the spaces for the masks when the text is not entered yet.
     * By default, we will use whitespaces.
     */
    private ObjectProperty<Character> placeholderCharacter;
    public final Character getPlaceholderCharacter() {return placeholderCharacterProperty().get();}
    public final void setPlaceholderCharacter(Character value) {placeholderCharacterProperty().set(value);}
    public final ObjectProperty<Character> placeholderCharacterProperty() {
        if(placeholderCharacter == null) {
            placeholderCharacter = new SimpleObjectProperty<>(this, "placeholderCharacter", ' '){
                @Override
                protected void invalidated() {
                    initializeDefaultText();
                }
            };
        }
        return placeholderCharacter;
    }

    /**
     * the input mask.
     */
    private StringProperty inputMask;
    public final String getInputMask() {return inputMaskProperty().get();}
    public final void setInputMask(String value) {inputMaskProperty().set(value);}
    public final StringProperty inputMaskProperty() {
        if(inputMask == null) {
            inputMask = new SimpleStringProperty(this, "inputMask"){
                @Override
                protected void invalidated() {
                    setPromptText(getInputMask());
                    initializeDefaultText();
                }
            };
        }
        return inputMask;
    }

    /**  the required mask. */
    private StringProperty requiredMask;
    public final String getRequiredMask() {return requiredMaskProperty().get();}
    public final void setRequiredMask(String value) {requiredMaskProperty().set(value);}
    public final StringProperty requiredMaskProperty() {
        if(requiredMask == null) {
            requiredMask = new SimpleStringProperty(this, "requiredMask");
        }
        return requiredMask;
    }

    /** the conversion mask. */
    private StringProperty conversionMask;
    public final String getConversionMask() {return conversionMaskProperty().get();}
    public final void setConversionMask(String value) {conversionMaskProperty().set(value);}
    public final StringProperty conversionMaskProperty() {
        if(conversionMask == null) {
            conversionMask = new SimpleStringProperty(this, "conversionMask");
        }
        return conversionMask;
    }

    /** the valid characters. */
    private StringProperty validCharacters;
    public final String getValidCharacters() {return validCharactersProperty().get();}
    public final void setValidCharacters(String value) {validCharactersProperty().set(value);}
    public final StringProperty validCharactersProperty() {
        if(validCharacters == null) {
            validCharacters = new SimpleStringProperty(this, "validCharacters");
        }
        return validCharacters;
    }

    /** the invalid characters. */
    private StringProperty invalidCharacters;
    public final String getInvalidCharacters() {return invalidCharactersProperty().get();}
    public final void setInvalidCharacters(String value) {invalidCharactersProperty().set(value);}
    public final StringProperty invalidCharactersProperty() {
        if(invalidCharacters == null) {
            invalidCharacters = new SimpleStringProperty(this, "invalidCharacters");
        }
        return invalidCharacters;
    }

    /**  the initial text. It will be used at the initial text on the field if setText was never called. */
    private StringProperty initialText;
    public final String getInitialText() {return initialTextProperty().get();}
    public final void setInitialText(String value) {initialTextProperty().set(value);}
    public final StringProperty initialTextProperty() {
        if(initialText == null) {
            initialText = new SimpleStringProperty(this, "initialText"){
                @Override
                protected void invalidated() {
                    initializeDefaultText();
                }
            };
        }
        return initialText;
    }

    /**
     * Checks if the auto-advance flag. If true, the caret will move to the next position if the typed letter is not
     * valid at the current position. We will check the next position to find out if the typed letter is valid. If not,
     * we will continue util finding a valid position for the letter, or reaching the end.
     */
    private BooleanProperty autoAdvance;
    public final boolean isAutoAdvance() {return autoAdvanceProperty().get();}
    public final void setAutoAdvance(boolean value) {autoAdvanceProperty().set(value);}
    public final BooleanProperty autoAdvanceProperty() {
        if(autoAdvance == null) {
            autoAdvance = new SimpleBooleanProperty(this, "autoAdvance", true);
        }
        return autoAdvance;
    }

    //@formatter:on


    protected void initializeMaskVerifiers(ObservableMap<Character, MaskVerifierCb> maskVerifiers) {
        maskVerifiers.put(INPUT_MASK_LETTER, Character::isLetter);
        maskVerifiers.put(INPUT_MASK_DIGIT_OR_LETTER, Character::isLetterOrDigit);
        maskVerifiers.put(INPUT_MASK_ANY_NON_SPACE, c -> !Character.isSpaceChar(c));
        maskVerifiers.put(INPUT_MASK_HAX, c -> ((c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') || Character.isDigit(c)));
        maskVerifiers.put(INPUT_MASK_DIGIT_NON_ZERO, c -> Character.isDigit(c) && c != '0');
        maskVerifiers.put(INPUT_MASK_DIGIT, Character::isDigit);
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_8, c -> (c >= '0' && c <= '8'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_7, c -> (c >= '0' && c <= '7'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_6, c -> (c >= '0' && c <= '6'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_5, c -> (c >= '0' && c <= '5'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_4, c -> (c >= '0' && c <= '4'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_3, c -> (c >= '0' && c <= '3'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_2, c -> (c >= '0' && c <= '2'));
        maskVerifiers.put(INPUT_MASK_DIGIT_0_TO_1, c -> (c >= '0' && c <= '1'));
        maskVerifiers.put(INPUT_MASK_DIGIT_ZERO, c -> (c == '0'));
    }

    protected void initializeConversions(ObservableMap<Character, ConversionCb> conversions) {
        conversions.put(CONVERSION_MASK_UPPER_CASE, Character::toUpperCase);
        conversions.put(CONVERSION_MASK_LOWER_CASE, Character::toLowerCase);
    }

    private void initializeListener() {
        getMaskVerifiers().addListener((InvalidationListener) e -> initializeDefaultText());
        textProperty().addListener(e -> log.warn("changeText: {}", getText()));
    }

    private String getFixedText() {
        if (fixedText == null) {
            fixedText = getInitialTextFromMask();
        }
        return fixedText;
    }

    private String getInitialTextFromMask() {
        String mask = getInputMask();
        if (mask != null) {
            for (Character c : getMaskVerifiers().keySet()) {
                mask = mask.replace(c, getPlaceholderCharacter());
            }
        }
        return mask;
    }

    private String keepFixedText(String text) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            final String s = text.substring(i, i + 1);
            final boolean hasFix = getFixedText().contains(s);
            buf.append(hasFix ? s : getPlaceholderCharacter());
        }
        return buf.toString();
    }

    private boolean hasMaskVerifier(Character mask) {
        return getMaskVerifiers().containsKey(mask);
    }

    private char convert(char c, int index) {
        String conversionMask = getConversionMask();

        if (Character.isSpaceChar(c) && hasMaskVerifier(getInputMask().charAt(index))) {
            return getPlaceholderCharacter();
        }

        if (conversionMask != null && index < conversionMask.length()) {
            ConversionCb callback = getConversions().get(conversionMask.charAt(index));
            if (callback != null) return callback.call(c);
        }

        return c;
    }

    private boolean verifyChar(char c, int index) {
        String mask = getInputMask();
        if (mask != null && index < mask.length()) {

            // if it is a space, accept it if the requiredMask is defined and it said not required
            char maskChar = mask.charAt(index);
            if (maskChar == c) return true;

            if (Character.isSpaceChar(c) || c == getPlaceholderCharacter()) {
                String requiredMask = getRequiredMask();
                if (requiredMask != null && index < requiredMask.length()) {
                    return requiredMask.charAt(index) == REQUIRED_MASK_NOT_REQUIRED;
                }
            }

            Callback<Character, Boolean> callback = getMaskVerifiers().get(maskChar);

            String validCharacters = getValidCharacters();
            String invalidCharacters = getInvalidCharacters();


            return (Objects.isBlank(validCharacters) || validCharacters.contains(String.valueOf(convert(c, index))))
                    && (Objects.isBlank(invalidCharacters) || !invalidCharacters.contains(String.valueOf(convert(c, index))))
                    && (callback != null ? callback.call(convert(c, index)) : maskChar == convert(c, index));
        } //
        else return false;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MaskFieldSkin(this);
    }

    /**
     * {@inheritDoc}
     */
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the {@code CssMetaData} of its superclasses.
     *
     * @return the {@code CssMetaData}
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    /**
     * Methods for creating instances of StyleableProperty
     */
    static final CssFactory<MaskField> FACTORY = new CssFactory<>(javafx.scene.control.TextField.getClassCssMetaData()) {
        @Override
        protected void registerCss() {
        }
    };


    /**
     * A static method which will create a MaskField for SSN (social security number, a 9-digit id number used in
     * the United States for tax purposes). The InputMask for it is "999-99-9999". The ConversionMask and the RequiredMask
     * are not set which means no conversion and all positions are required.
     *
     * @return a MaskField for SSN.
     */
    public static MaskField createSSNField() {
        MaskField field = new MaskField();
        field.setInputMask("999-99-9999");
        field.setPlaceholderCharacter('#');
        return field;
    }

    /**
     * A static method which will create a MaskField for Phone Number in US and Canada. The InputMask for it is
     * "(999) 999-9999". The ConversionMask and the RequiredMask are not set which means no conversion and all positions
     * are required.
     *
     * @return a MaskField for a Phone Number in US and Canada.
     */
    public static MaskField createPhoneNumberField() {
        MaskField field = new MaskField();
        field.setInputMask("(999) 999-9999");
        return field;
    }

    /**
     * A static method which will create a MaskField for the Zip Code in US. The InputMask for it is "99999". The
     * ConversionMask and the RequiredMask are not set which means no conversion and all positions are required.
     *
     * @return a MaskField for a Phone Number in US and Canada.
     */
    public static MaskField createZipCodeField() {
        MaskField field = new MaskField();
        field.setInputMask("99999");
        return field;
    }

    /**
     * A static method which will create a MaskField for the Zip Code + 4 in US. The InputMask for it is
     * "99999-9999". The ConversionMask and the RequiredMask are not set which means no conversion and all positions are
     * required.
     *
     * @return a MaskField for a Phone Number in US and Canada.
     */
    public static MaskField createZipCodePlus4Field() {
        MaskField field = new MaskField();
        field.setInputMask("99999-9999");
        return field;
    }

    /**
     * A static method which will create a MaskField for the Zip Code + 4 in US. The InputMask for it is
     * "99999-9999". The ConversionMask and the RequiredMask are not set which means no conversion and all positions are
     * required.
     *
     * @return a MaskField for a Phone Number in US and Canada.
     */
    public static MaskField createCanadaPostalCodeField() {
        MaskField field = new MaskField();
        field.setInputMask("A9A 9A9"); //NON-NLS
        field.setConversionMask("U_U _U_"); //NON-NLS
        field.setPlaceholderCharacter('_');
        field.setAutoAdvance(false);
        return field;
    }

    /**
     * A static method which will create a MaskField for serial number. This particular one is commonly used by
     * Microsoft's recent products. The InputMask for it is "NNNNN-NNNNN-NNNNN-NNNNN-NNNNN". The ConversionMask is
     * "UUUUU_UUUUU_UUUUU_UUUUU_UUUUU". The RequiredMask are not set which means all positions are required.
     *
     * @return a MaskField for a serial number.
     */
    public static MaskField createSerialNumberField() {
        MaskField field = new MaskField();
        field.setInputMask("NNNNN-NNNNN-NNNNN-NNNNN-NNNNN"); //NON-NLS
        field.setConversionMask("UUUUU_UUUUU_UUUUU_UUUUU_UUUUU"); //NON-NLS
        return field;
    }

    /**
     * A static method which will create a MaskField for IPv6 address. The InputMask for it is
     * "HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH". The ConversionMask is "LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL" so
     * that only lower case letters will be used, although according to the standard, the case shouldn't matter. The
     * RequiredMask are not set which means all positions are required.
     *
     * @return a MaskField for IPv6 address.
     */
    public static MaskField createIPv6Field() {
        MaskField field = new MaskField();
        field.setInputMask("HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH:HHHH"); //NON-NLS
        field.setConversionMask("LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL_LLLL"); //NON-NLS
        return field;
    }

    /**
     * A static method which will create a MaskField for MAC address. The InputMask for it is "HH:HH:HH:HH:HH:HH".
     * The ConversionMask is "LL:LL:LL:LL:LL:LL" so that only lower case letters will be used, although according to the
     * standard, the case shouldn't matter. The RequiredMask are not set which means all positions are required.
     *
     * @return a MaskField for MAC address.
     */
    public static MaskField createMacAddressField() {
        MaskField field = new MaskField();
        field.setInputMask("HH:HH:HH:HH:HH:HH"); //NON-NLS
        field.setConversionMask("LL:LL:LL:LL:LL:LL"); //NON-NLS
        return field;
    }

    /**
     * A static method which will create a MaskField for the Date in US standard format (in the order of
     * month/day/year). The InputMask for it is "19/39/9999". The ConversionMask is "_x/_y/____" and we defined
     * conversion callbacks for both 'x' and 'y' to make sure the month is between 1 and 12, the day of the month is
     * between 1 and 31.
     * <p>
     * The initial text was set to the today's date.
     *
     * @return a MaskField for Date.
     */
    public static MaskField createDateField() {
        MaskField field = new MaskField();
        field.setInputMask("19/39/9999");

        field.setConversionMask("_x/_y/____");
        field.getConversions().put('x', param -> {
            char c = field.getText().charAt(0);
            return c == '1' && param > '2' ? '2' : param;
        });
        field.getConversions().put('y', param -> {
            char c = field.getText().charAt(3);
            return c == '3' && param > '1' ? '1' : param;
        });

        field.setInitialText(LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        return field;
    }

    /**
     * A static method which will create a MaskField for the Time in the 12-hour format (in the order of
     * hour/minute/second AM/PM). The InputMask for it is "19:59:59 xm". The InputMask 'x' and 'm' were defined to make
     * sure the entered letter is 'A' or 'P', and 'M' respectively. The ConversionMask is "_x:__:__ UU" and we defined
     * conversion callbacks for the conversion mask 'x' to make sure the hour is between 1 and 12.
     * <p>
     * The initial text was set to the current time.
     *
     * @return a MaskField for Date.
     */
    public static MaskField createTime12Field() {
        MaskField field = new MaskField();

        field.setInputMask("19:59:59 xm");
        field.getMaskVerifiers().put('x', c -> c == 'A' || c == 'P');
        field.getMaskVerifiers().put('m', c -> c == 'M');

        field.setConversionMask("_x:__:__ UU");
        field.getConversions().put('x', c -> {
            char firstChar = field.getText().charAt(0);
            if (firstChar == '1' && c > '2') return '2';
            if (firstChar == '0' && c == '0') return '1';
            else return c;
        });

        field.setInitialText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss ZZZ")));

        return field;
    }

    /**
     * A static method which will create a MaskField for the Time in the 24-hour format (in the order of
     * hour/minute/second). The InputMask for it is "29/59/59". The ConversionMask is "_x/__/__" and we defined
     * conversion callbacks for the conversion mask named 'x' to make sure the hour is between 0 and 23.
     * <p>
     * The initial text was set to the current time.
     *
     * @return a MaskField for Date.
     */
    public static MaskField createTime24Field() {
        MaskField field = new MaskField();
        field.setInputMask("29:59:59");

        field.setConversionMask("_x:__:__"); //NON-NLS
        field.getConversions().put('x', c -> {
            char firstChar = field.getText().charAt(0);
            if (firstChar == '2' && c > '3') {
                return '3';
            }
            return c;
        });

        field.setInitialText(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
        return field;
    }


    @FunctionalInterface
    public interface MaskVerifierCb extends Callback<Character, Boolean> {

    }

    @FunctionalInterface
    public interface ConversionCb extends Callback<Character, Character> {

    }

    public static final char INPUT_MASK_LETTER = 'A';
    public static final char INPUT_MASK_DIGIT_OR_LETTER = 'N';
    public static final char INPUT_MASK_ANY_NON_SPACE = 'X';
    public static final char INPUT_MASK_HAX = 'H';
    public static final char INPUT_MASK_DIGIT_NON_ZERO = 'D';
    public static final char INPUT_MASK_DIGIT = '9';
    public static final char INPUT_MASK_DIGIT_0_TO_8 = '8';
    public static final char INPUT_MASK_DIGIT_0_TO_7 = '7';
    public static final char INPUT_MASK_DIGIT_0_TO_6 = '6';
    public static final char INPUT_MASK_DIGIT_0_TO_5 = '5';
    public static final char INPUT_MASK_DIGIT_0_TO_4 = '4';
    public static final char INPUT_MASK_DIGIT_0_TO_3 = '3';
    public static final char INPUT_MASK_DIGIT_0_TO_2 = '2';
    public static final char INPUT_MASK_DIGIT_0_TO_1 = '1';
    public static final char INPUT_MASK_DIGIT_ZERO = '0';
    public static final char CONVERSION_MASK_UPPER_CASE = 'U';
    public static final char CONVERSION_MASK_LOWER_CASE = 'L';
    public static final char CONVERSION_MASK_IGNORE = '_';
    public static final char REQUIRED_MASK_REQUIRED = 'R';
    public static final char REQUIRED_MASK_NOT_REQUIRED = '_';

}