
// remove a set from a original set
export const removeAllSet = (originalSet, removedSet) => {
    removedSet.forEach(Set.prototype.delete, originalSet);
}
