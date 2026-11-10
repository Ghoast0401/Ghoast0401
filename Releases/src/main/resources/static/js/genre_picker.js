/**
 * ジャンル選択パンくずUI
 *
 * 使い方（HTML側）:
 *   <script th:inline="javascript">
 *     window.genreTree = /*[[${genreGroups}]]* /;
 *   </script>
 *   <script th:src="@{/js/genre-picker.js}"></script>
 *   <script>
 *     initGenrePicker({
 *       treeVar: window.genreTree,
 *       breadcrumbId: 'genre-breadcrumb',
 *       optionsId: 'genre-options',
 *       displayId: 'genre-selected-display',
 *       hiddenInputId: 'genre'
 *     });
 *   </script>
 *
 * データ構造:
 *   [{ label: "和食系", categories: [
 *        { label: "ラーメン", children: ["家系","二郎系"] },
 *        { label: "和食", children: [] }
 *   ]}, ...]
 */
function initGenrePicker(options) {
	const genreTree = Array.isArray(options.treeVar) ? options.treeVar : [];
	if (!Array.isArray(options.treeVar)) {
		console.error('genreTree is not an array. Controller側で genreGroups がModelに渡されているか確認してください。', options.treeVar);
	}

	const crumbEl = document.getElementById(options.breadcrumbId);
	const optionsEl = document.getElementById(options.optionsId);
	const displayEl = document.getElementById(options.displayId);
	const hiddenInput = document.getElementById(options.hiddenInputId);

	let genrePath = [];

	function buildGenreValue(categoryLabel, childLabel) {
		return childLabel ? `${categoryLabel}（${childLabel}）` : categoryLabel;
	}

	// genrePathの深さに応じて、今表示すべき選択肢一覧を返す
	function getCurrentLevelItems() {
		if (genrePath.length === 0) {
			return genreTree.map(g => ({ label: g.label, hasChildren: true }));
		}
		const group = genreTree.find(g => g.label === genrePath[0]);
		if (!group) return [];

		if (genrePath.length === 1) {
			return group.categories.map(c => ({ label: c.label, hasChildren: c.children.length > 0 }));
		}

		const category = group.categories.find(c => c.label === genrePath[1]);
		if (!category || genrePath.length !== 2) return [];

		return category.children.map(child => ({ label: child, hasChildren: false }));
	}

	function renderBreadcrumb() {
		crumbEl.innerHTML = '';

		const rootCrumb = document.createElement('span');
		rootCrumb.className = 'crumb' + (genrePath.length === 0 ? ' current' : '');
		rootCrumb.textContent = 'ジャンルを選択';
		rootCrumb.addEventListener('click', () => { genrePath = []; render(); });
		crumbEl.appendChild(rootCrumb);

		genrePath.forEach((label, idx) => {
			crumbEl.appendChild(document.createTextNode(' > '));
			const seg = document.createElement('span');
			seg.className = 'crumb' + (idx === genrePath.length - 1 ? ' current' : '');
			seg.textContent = label;
			seg.addEventListener('click', () => { genrePath = genrePath.slice(0, idx + 1); render(); });
			crumbEl.appendChild(seg);
		});
	}

	function handleSelect(item) {
		if (item.hasChildren) {
			genrePath = [...genrePath, item.label];
			render();
			return;
		}
		const value = genrePath.length === 2
			? buildGenreValue(genrePath[1], item.label)
			: buildGenreValue(item.label, null);
		hiddenInput.value = value;
		displayEl.textContent = '選択中: ' + value;
		render();
	}

	function renderOptions() {
		optionsEl.innerHTML = '';
		getCurrentLevelItems().forEach(item => {
			const btn = document.createElement('button');
			btn.type = 'button';
			btn.textContent = item.label + (item.hasChildren ? ' ›' : '');

			const currentValue = !item.hasChildren
				? (genrePath.length === 2 ? buildGenreValue(genrePath[1], item.label) : buildGenreValue(item.label, null))
				: null;
			if (currentValue && hiddenInput.value === currentValue) {
				btn.classList.add('is-selected');
			}

			btn.addEventListener('click', () => handleSelect(item));
			optionsEl.appendChild(btn);
		});
	}

	function render() {
		renderBreadcrumb();
		renderOptions();
	}

	// 編集時など既に値が入っている場合、そのグループ・カテゴリーまでパンくずを進めておく
	(function initFromExistingValue() {
		const existing = hiddenInput.value;
		if (!existing) return;

		for (const group of genreTree) {
			for (const category of group.categories) {
				if (existing === category.label) {
					genrePath = [group.label];
					displayEl.textContent = '選択中: ' + existing;
					return;
				}
				const matchedChild = category.children.find(child => existing === buildGenreValue(category.label, child));
				if (matchedChild) {
					genrePath = [group.label, category.label];
					displayEl.textContent = '選択中: ' + existing;
					return;
				}
			}
		}
	})();

	render();
}