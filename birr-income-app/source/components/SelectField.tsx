import React, { useState } from 'react';
import {
  FlatList,
  Modal,
  Pressable,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { useColors } from '@/hooks/useColors';
import { Feather } from '@expo/vector-icons';
import * as Haptics from 'expo-haptics';

interface Option {
  label: string;
  value: number;
}

interface SelectFieldProps {
  label: string;
  value: number;
  options: Option[];
  onChange: (value: number) => void;
}

export function SelectField({ label, value, options, onChange }: SelectFieldProps) {
  const colors = useColors();
  const [open, setOpen] = useState(false);
  const selected = options.find((o) => o.value === value);

  return (
    <View style={{ flex: 1 }}>
      <Text style={[styles.label, { color: colors.mutedForeground }]}>{label}</Text>
      <Pressable
        onPress={() => {
          Haptics.selectionAsync();
          setOpen(true);
        }}
        style={[
          styles.field,
          { backgroundColor: colors.secondary, borderColor: colors.border },
        ]}
        testID={`select-${label.toLowerCase()}`}
      >
        <Text style={[styles.fieldText, { color: colors.foreground }]} numberOfLines={1}>
          {selected?.label ?? '—'}
        </Text>
        <Feather name="chevron-down" size={16} color={colors.mutedForeground} />
      </Pressable>

      <Modal visible={open} transparent animationType="fade" onRequestClose={() => setOpen(false)}>
        <Pressable style={styles.backdrop} onPress={() => setOpen(false)}>
          <View />
        </Pressable>
        <View style={[styles.sheet, { backgroundColor: colors.card, borderColor: colors.border }]}>
          <Text style={[styles.sheetTitle, { color: colors.foreground }]}>{label}</Text>
          <FlatList
            data={options}
            keyExtractor={(item) => String(item.value)}
            style={{ maxHeight: 320 }}
            renderItem={({ item }) => {
              const isActive = item.value === value;
              return (
                <Pressable
                  onPress={() => {
                    Haptics.selectionAsync();
                    onChange(item.value);
                    setOpen(false);
                  }}
                  style={[
                    styles.option,
                    isActive && { backgroundColor: colors.secondary },
                  ]}
                >
                  <Text
                    style={[
                      styles.optionText,
                      { color: isActive ? colors.primary : colors.foreground },
                    ]}
                  >
                    {item.label}
                  </Text>
                  {isActive && <Feather name="check" size={16} color={colors.primary} />}
                </Pressable>
              );
            }}
          />
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  label: {
    fontSize: 11,
    fontWeight: '600',
    textTransform: 'uppercase',
    letterSpacing: 0.4,
    marginBottom: 6,
  },
  field: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    borderWidth: 1,
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 12,
  },
  fieldText: {
    fontSize: 15,
    fontWeight: '600',
    flexShrink: 1,
  },
  backdrop: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.35)',
  },
  sheet: {
    position: 'absolute',
    left: 20,
    right: 20,
    top: '25%',
    borderRadius: 16,
    borderWidth: 1,
    padding: 12,
    maxHeight: 400,
  },
  sheetTitle: {
    fontSize: 15,
    fontWeight: '700',
    marginBottom: 8,
    paddingHorizontal: 8,
  },
  option: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 12,
    paddingVertical: 12,
    borderRadius: 10,
  },
  optionText: {
    fontSize: 15,
    fontWeight: '500',
  },
});
