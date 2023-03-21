export class Stack {
    private elements = [];
    static readonly TAB_PARAM = 'sezioneSelezionata';
  
    constructor(...elements) {
      // Initializing the stack with given arguments 
      this.elements = [...elements];
    }
    // Proxying the push/shift methods
    push(...args) {
      return this.elements.push(...args);
    }
    pushIfNotExist(args) {
      if (args.includes(Stack.TAB_PARAM) && this.elements.find(e => e.includes(Stack.TAB_PARAM)))
        return;
      if (!this.elements.includes(args))
        return this.elements.push(args);
    }
    pop() {
      return this.elements.pop();
    }
    // Add some length utility methods
    getLength(...args) {
      return this.elements.length;
    }
    setLength(length) {
      return this.elements.length = length;
    }
  }