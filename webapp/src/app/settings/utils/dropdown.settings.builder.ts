export class DropdownSettingsBuilder {
  private singleSelection = false;
  private text = "Odaberite";
  private enableCheckAll = false;
  private enableSearchFilter = true;
  private enableFilterSelectAll = false;
  private noDataLabel = 'Nema dostupnih podataka';
  private searchPlaceholderText = 'Pretražite';
  private showCheckbox = true;
  private labelKey = 'itemName';

  setSingleSelection(singleSelection: boolean) {
    this.singleSelection = singleSelection;
    return this;
  }

  setText(text: string) {
    this.text = text;
    return this;
  }

  setEnableCheckAll(enableCheckAll: boolean) {
    this.enableCheckAll = enableCheckAll;
    return this;
  }

  setEnableSearchFilter(enableSearchFilter: boolean) {
    this.enableSearchFilter = enableSearchFilter;
    return this;
  }

  setenableFilterSelectAll(enableFilterSelectAll: boolean) {
    this.enableFilterSelectAll = enableFilterSelectAll;
    return this;
  }

  setSearchPlaceholderText(searchPlaceholderText: string) {
    this.searchPlaceholderText = searchPlaceholderText;
    return this;
  }

  setShowCheckbox(showCheckbox: boolean) {
    this.showCheckbox = showCheckbox;
    return this;
  }

  setLabelKey(labelKey: string) {
    this.labelKey = labelKey;
    return this;
  }

  build() {
    return {
      singleSelection : this.singleSelection,
      text : this.text,
      enableCheckAll : this.enableCheckAll,
      enableSearchFilter : this.enableSearchFilter,
      enableFilterSelectAll : this.enableFilterSelectAll,
      noDataLabel : this.noDataLabel,
      searchPlaceholderText : this.searchPlaceholderText,
      showCheckbox : this.showCheckbox
    }
  }

}
