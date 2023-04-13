export class ScrollSupport {
  public static scrollToTop() {
    window.scrollTo({top: 0, behavior: 'smooth'});
  }

  public static scroll(id: number) {
    const elementById = document.getElementById('item-' + id);
    elementById.scrollIntoView({behavior: 'smooth', block: 'start', inline: 'nearest'});
  }
}

