const fs = require('fs');
const zlib = require('zlib');
function crc32(buf){
  const table = Array.from({length:256}, (_,n)=>{
    let c = n;
    for(let k=0;k<8;k++) c = (c & 1) ? 0xedb88320 ^ (c >>> 1) : c >>> 1;
    return c >>> 0;
  });
  let crc = 0xffffffff;
  for (const b of buf) crc = table[(crc ^ b) & 0xff] ^ (crc >>> 8);
  return (crc ^ 0xffffffff) >>> 0;
}
function chunk(type, data){
  const len = Buffer.alloc(4);
  len.writeUInt32BE(data.length, 0);
  const typeBuf = Buffer.from(type);
  const crc = Buffer.alloc(4);
  crc.writeUInt32BE(crc32(Buffer.concat([typeBuf, data])), 0);
  return Buffer.concat([len, typeBuf, data, crc]);
}
function createPng(width, height, rgba){
  const header = Buffer.from([0x89,0x50,0x4e,0x47,0x0d,0x0a,0x1a,0x0a]);
  const ihdr = Buffer.alloc(13);
  ihdr.writeUInt32BE(width, 0);
  ihdr.writeUInt32BE(height, 4);
  ihdr[8] = 8;
  ihdr[9] = 6;
  ihdr[10] = 0;
  ihdr[11] = 0;
  ihdr[12] = 0;
  const raw = Buffer.alloc((4 * width + 1) * height);
  for (let y = 0; y < height; y++) {
    const rowStart = y * (4 * width + 1);
    raw[rowStart] = 0;
    for (let x = 0; x < width; x++) {
      const pos = rowStart + 1 + x * 4;
      raw[pos] = rgba[0];
      raw[pos + 1] = rgba[1];
      raw[pos + 2] = rgba[2];
      raw[pos + 3] = rgba[3];
    }
  }
  const idat = zlib.deflateSync(raw);
  return Buffer.concat([header, chunk('IHDR', ihdr), chunk('IDAT', idat), chunk('IEND', Buffer.alloc(0))]);
}
const iconsDir = './icons';
if (!fs.existsSync(iconsDir)) fs.mkdirSync(iconsDir);
const blue = [37, 99, 235, 255];
fs.writeFileSync(`${iconsDir}/icon-192.png`, createPng(192, 192, blue));
fs.writeFileSync(`${iconsDir}/icon-512.png`, createPng(512, 512, blue));
console.log('Created icons');
