import React, { useEffect, useState } from 'react';
import {
  Modal,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useColors } from '@/hooks/useColors';
import { Feather } from '@expo/vector-icons';
import * as Haptics from 'expo-haptics';
import { formatEthiopianDate, gregorianToEthiopian } from '@/lib/ethiopian-calendar';
import { useEntries } from '@/context/EntriesContext';

interface EntryModalProps {
  isoDate: string | null;
  onClose: () => void;
}

export function EntryModal({ isoDate, onClose }: EntryModalProps) {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { getEntry, upsertEntry, deleteEntry } = useEntries();
  const [amount, setAmount] = useState('');
  const [note, setNote] = useState('');

  const existing = isoDate ? getEntry(isoDate) : undefined;

  useEffect(() => {
    if (isoDate) {
      const entry = getEntry(isoDate);
      setAmount(entry ? String(entry.amount) : '');
      setNote(entry?.note ?? '');
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isoDate]);

  if (!isoDate) return null;

  const ethiopian = formatEthiopianDate(gregorianToEthiopian(isoDate));
  const parsedAmount = parseFloat(amount);
  const canSave = amount.trim().length > 0 && !Number.isNaN(parsedAmount) && parsedAmount >= 0;

  async function handleSave() {
    if (!canSave || !isoDate) return;
    Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
    await upsertEntry(isoDate, parsedAmount, note.trim() || null);
    onClose();
  }

  async function handleDelete() {
    if (!isoDate) return;
    Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
    await deleteEntry(isoDate);
    onClose();
  }

  return (
    <Modal visible transparent animationType="slide" onRequestClose={onClose}>
      <Pressable style={styles.backdrop} onPress={onClose} />
      <View
        style={[
          styles.sheet,
          {
            backgroundColor: colors.card,
            borderColor: colors.border,
            paddingBottom: Math.max(insets.bottom, 20),
          },
        ]}
      >
        <View style={styles.handle} />

        <View style={styles.headerRow}>
          <View style={{ flex: 1 }}>
            <Text style={[styles.dateLabel, { color: colors.mutedForeground }]}>
              {existing ? 'Edit entry' : 'New entry'}
            </Text>
            <Text style={[styles.dateValue, { color: colors.foreground }]}>{ethiopian}</Text>
          </View>
          <Pressable
            onPress={onClose}
            style={[styles.closeButton, { backgroundColor: colors.secondary }]}
            testID="close-entry-modal"
          >
            <Feather name="x" size={18} color={colors.foreground} />
          </Pressable>
        </View>

        <View style={{ marginTop: 20, gap: 16 }}>
          <View>
            <Text style={[styles.fieldLabel, { color: colors.mutedForeground }]}>
              Amount (ETB)
            </Text>
            <View
              style={[
                styles.amountRow,
                { backgroundColor: colors.secondary, borderColor: colors.border },
              ]}
            >
              <Text style={[styles.currencyPrefix, { color: colors.mutedForeground }]}>ETB</Text>
              <TextInput
                value={amount}
                onChangeText={setAmount}
                placeholder="0"
                placeholderTextColor={colors.mutedForeground}
                keyboardType="decimal-pad"
                style={[styles.amountInput, { color: colors.foreground }]}
                testID="amount-input"
                autoFocus
              />
            </View>
          </View>

          <View>
            <Text style={[styles.fieldLabel, { color: colors.mutedForeground }]}>
              Note (optional)
            </Text>
            <TextInput
              value={note}
              onChangeText={setNote}
              placeholder="What was this from?"
              placeholderTextColor={colors.mutedForeground}
              style={[
                styles.noteInput,
                { backgroundColor: colors.secondary, borderColor: colors.border, color: colors.foreground },
              ]}
              testID="note-input"
              multiline
            />
          </View>
        </View>

        <View style={styles.actionsRow}>
          {existing && (
            <Pressable
              onPress={handleDelete}
              style={[styles.deleteButton, { borderColor: colors.destructive }]}
              testID="delete-entry"
            >
              <Feather name="trash-2" size={16} color={colors.destructive} />
              <Text style={[styles.deleteButtonText, { color: colors.destructive }]}>Delete</Text>
            </Pressable>
          )}
          <Pressable
            onPress={handleSave}
            disabled={!canSave}
            style={[
              styles.saveButton,
              { backgroundColor: canSave ? colors.primary : colors.muted },
            ]}
            testID="save-entry"
          >
            <Text
              style={[
                styles.saveButtonText,
                { color: canSave ? colors.primaryForeground : colors.mutedForeground },
              ]}
            >
              Save
            </Text>
          </Pressable>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.4)',
  },
  sheet: {
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    borderWidth: 1,
    borderBottomWidth: 0,
    paddingHorizontal: 20,
    paddingTop: 10,
    ...Platform.select({ web: { paddingBottom: 34 } }),
  },
  handle: {
    alignSelf: 'center',
    width: 36,
    height: 4,
    borderRadius: 2,
    backgroundColor: 'rgba(0,0,0,0.15)',
    marginBottom: 14,
  },
  headerRow: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    justifyContent: 'space-between',
  },
  dateLabel: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
    letterSpacing: 0.5,
  },
  dateValue: {
    fontSize: 22,
    fontWeight: '800',
    marginTop: 2,
  },
  closeButton: {
    width: 34,
    height: 34,
    borderRadius: 17,
    alignItems: 'center',
    justifyContent: 'center',
  },
  fieldLabel: {
    fontSize: 11,
    fontWeight: '700',
    textTransform: 'uppercase',
    letterSpacing: 0.4,
    marginBottom: 8,
  },
  amountRow: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderRadius: 14,
    paddingHorizontal: 14,
  },
  currencyPrefix: {
    fontSize: 16,
    fontWeight: '700',
    marginRight: 8,
  },
  amountInput: {
    flex: 1,
    fontSize: 22,
    fontWeight: '800',
    paddingVertical: 14,
  },
  noteInput: {
    borderWidth: 1,
    borderRadius: 14,
    paddingHorizontal: 14,
    paddingVertical: 12,
    fontSize: 15,
    minHeight: 56,
    textAlignVertical: 'top',
  },
  actionsRow: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 24,
  },
  deleteButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 6,
    borderWidth: 1.5,
    borderRadius: 14,
    paddingHorizontal: 18,
    paddingVertical: 14,
  },
  deleteButtonText: {
    fontSize: 15,
    fontWeight: '700',
  },
  saveButton: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 14,
    paddingVertical: 14,
  },
  saveButtonText: {
    fontSize: 16,
    fontWeight: '800',
  },
});
