// https://github.com/cljsjs/packages/wiki/Creating-Externs

var speechSynthesis = {
  getVoices: function() {},
  speak: function() {}
}

/** @interface */
function SpeechSynthesisUtterance(phrase) {}
SpeechSynthesisUtterance.prototype.voice = {}
