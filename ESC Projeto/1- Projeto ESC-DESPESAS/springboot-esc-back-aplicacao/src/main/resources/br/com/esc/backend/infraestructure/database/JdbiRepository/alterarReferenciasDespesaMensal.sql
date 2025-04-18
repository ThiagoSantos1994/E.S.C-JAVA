-- Atualiza o nome da despesa atual para a despesa referencia
UPDATE
	tbd_DespesaMensal
SET
	ds_NomeDespesa = (
	    SELECT TOP 1 ds_NomeDespesa
	    FROM tbd_DespesaMensal
	    WHERE id_DetalheDespesa = :idDetalheDespesaNova AND id_Funcionario = :idFuncionario
	    ORDER BY id_Despesa DESC
	    )
WHERE
	id_Despesa >= :idDespesa
	AND id_DetalheDespesa = :idDetalheDespesa
	AND id_Funcionario = :idFuncionario

-- Altera o id despesa mensal para a despesa referencia
UPDATE
	tbd_DespesaMensal
SET
	id_DetalheDespesa = :idDetalheDespesaNova
WHERE
	id_Despesa >= :idDespesa
	AND id_DetalheDespesa = :idDetalheDespesa
	AND id_Funcionario = :idFuncionario

-- Altera o id despesa dos detalhes da despesa para a despesa referencia
UPDATE
	tbd_DetalheDespesasMensais
SET
	id_DetalheDespesa = :idDetalheDespesaNova
WHERE
	id_Despesa >= :idDespesa
	AND id_DetalheDespesa = :idDetalheDespesa
	AND id_Funcionario = :idFuncionario

-- Altera o id despesa dos logs para a despesa referencia
UPDATE
	tbd_DetalheDespesasMensaisLogs
SET
	id_DetalheDespesa = :idDetalheDespesaNova
WHERE
	id_Despesa >= :idDespesa
	AND id_DetalheDespesa = :idDetalheDespesa
	AND id_Funcionario = :idFuncionario

-- Altera o id despesa das observações para a despesa referencia
UPDATE
	tbd_ObservacaoDetalheDespesaMensal
SET
	id_DetalheDespesa = :idDetalheDespesaNova
WHERE
	id_Despesa >= :idDespesa
	AND id_DetalheDespesa = :idDetalheDespesa
	AND id_Funcionario = :idFuncionario